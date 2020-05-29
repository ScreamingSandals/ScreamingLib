package org.screamingsandals.lib.gamecore.core.cycle;

import com.google.common.base.Preconditions;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.debug.Debug;
import org.screamingsandals.lib.gamecore.GameCore;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.core.GameState;
import org.screamingsandals.lib.gamecore.core.GameType;
import org.screamingsandals.lib.gamecore.core.phase.GamePhase;
import org.screamingsandals.lib.gamecore.events.core.game.SGameTickEvent;
import org.screamingsandals.lib.gamecore.player.GamePlayer;
import org.screamingsandals.lib.tasker.BaseTask;

import java.util.*;

@EqualsAndHashCode(callSuper = false)
@Data
public abstract class GameCycle extends BaseTask {
    protected final transient GameFrame gameFrame;
    protected GamePhase currentPhase;
    protected Map<GameState, GamePhase> gamePhases = new LinkedHashMap<>();
    protected List<GamePhase> customPhases = new LinkedList<>();
    protected GameType gameType;

    @Override
    public void run() {
        final var gameState = gameFrame.getActiveState();
        if (currentPhase != null && currentPhase.getPhaseType() == gameState) {
            final var playersInGameSize = gameFrame.getPlayersInGame().size();
            if (gameState == GameState.WAITING && playersInGameSize == 0) {
                return;
            }

            tick();
            return;
        }

        switch (gameState) {
            case LOADING:
            case WAITING:
            case PRE_GAME_COUNTDOWN:
            case IN_GAME:
            case DEATHMATCH:
            case AFTER_GAME_COUNTDOWN:
            case RESTART: {
                currentPhase = gamePhases.get(gameState);
                break;
            }
            case CUSTOM: {
                final var iterator = customPhases.iterator();
                if (iterator.hasNext()) {
                    currentPhase = iterator.next();
                    iterator.remove();
                }
                break;
            }
            case DISABLED: {
                //why are we running?!
                stop();
                return;
            }
        }

        tick();
    }

    @Override
    public void stop() {
        kickAllPlayers();

        super.stop();

        currentPhase = null;
    }

    private void tick() {
        GameCore.fireEvent(new SGameTickEvent(gameFrame, currentPhase));
        Preconditions.checkNotNull(currentPhase, "Current phase cannot be null!").tick();
        gameFrame.getPlaceholderParser().reload();
        gameFrame.updateScoreboards();

        if (currentPhase.hasFinished() && currentPhase.getPhaseType() != GameState.CUSTOM) {
            setNextAndRemovePrevious();
        }
    }

    public void kickAllPlayers() {
        if (gameFrame.getPlayersInGame().isEmpty()) {
            return;
        }

        gameFrame.getPlayersInGame().forEach(this::kickPlayer);
    }

    public void kickPlayer(GamePlayer gamePlayer) {
        gameFrame.leave(gamePlayer);
    }

    public void addPhase(GameState gameState, GamePhase gamePhase) {
        if (gamePhases.get(gameState) != null) {
            Debug.warn("GamePhase for state " + gameState + " is already defined!");
            return;
        }

        gamePhase.setPhaseType(gameState);
        gamePhases.put(gameState, gamePhase);
    }

    public void removePhase(GameState gameState) {
        gamePhases.remove(gameState);
    }

    public void addCustomPhase(GamePhase gamePhase) {
        customPhases.add(gamePhase);
    }

    public boolean isRunning() {
        return currentPhase != null;
    }

    private void setNextAndRemovePrevious() {
        final var iterator = gamePhases.entrySet().iterator();

        if (iterator.hasNext()) {
            final var next = iterator.next();
            currentPhase = next.getValue();
            iterator.remove();
        }
    }
}
