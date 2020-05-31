package org.screamingsandals.lib.gamecore.core.cycle;

import com.google.common.base.Preconditions;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.debug.Debug;
import org.screamingsandals.lib.gamecore.GameCore;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.core.GameState;
import org.screamingsandals.lib.gamecore.core.phase.GamePhase;
import org.screamingsandals.lib.gamecore.error.ErrorType;
import org.screamingsandals.lib.gamecore.error.GameError;
import org.screamingsandals.lib.gamecore.events.core.game.SGameTickEvent;
import org.screamingsandals.lib.gamecore.player.GamePlayer;
import org.screamingsandals.lib.tasker.BaseTask;

import java.util.*;

@EqualsAndHashCode(callSuper = false)
@Data
public abstract class GameCycle extends BaseTask {
    protected final transient GameFrame gameFrame;
    protected GameCycleType gameCycleType;

    protected Map<GameState, GamePhase> availablePhases = new LinkedHashMap<>();
    protected List<GamePhase> availableCustomPhases = new LinkedList<>();

    protected GamePhase currentPhase;

    @Override
    public void run() {
        final var gameState = gameFrame.getActiveState();
        if (currentPhase != null && currentPhase.getPhaseType() == gameState) {
            final var playersInGameSize = gameFrame.getPlayersInGame().size();
            if (gameState == GameState.WAITING && playersInGameSize == 0) {
                //Why tick if nobody is in the game?
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
                currentPhase = availablePhases.get(gameState);

                if (currentPhase == null) {
                    GameCore.getErrorManager().newError(new GameError(gameFrame, ErrorType.UNKNOWN, null), true);
                    stop();
                }

                System.out.println("Phase switched! New phase=" + currentPhase.getPhaseType());
                break;
            }
            case CUSTOM: {
                final var iterator = availableCustomPhases.iterator();
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
        System.out.println("Tick!");
        GameCore.fireEvent(new SGameTickEvent(gameFrame, currentPhase));
        gameFrame.getPlaceholderParser().reload();
        gameFrame.updateScoreboards();

        try {
            Preconditions.checkNotNull(currentPhase, "Current phase cannot be null!").tick();
        } catch (Exception e) {
            GameCore.getErrorManager().newError(new GameError(gameFrame, ErrorType.UNKNOWN, e), true);
            stop();

            return;
        }

        gameFrame.updateScoreboards();

        //TODO: auto-switch to next phase
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
        if (availablePhases.get(gameState) != null) {
            Debug.warn("GamePhase for state " + gameState + " is already defined!");
            return;
        }

        gamePhase.setPhaseType(gameState);
        availablePhases.put(gameState, gamePhase);
    }

    public void removePhase(GameState gameState) {
        availablePhases.remove(gameState);
    }

    public void addCustomPhase(GamePhase gamePhase) {
        availableCustomPhases.add(gamePhase);
    }

    public boolean isRunning() {
        return currentPhase != null;
    }
}
