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
    protected CycleType cycleType;

    protected Map<GameState, GamePhase> availablePhases = new LinkedHashMap<>();
    protected List<GamePhase> availableCustomPhases = new LinkedList<>();

    protected GamePhase currentPhase;
    protected GamePhase previousPhase;

    /**
     * Main logic for ticking
     */
    @Override
    public void run() {
        final var gameState = gameFrame.getActiveState();
        if (currentPhase != null && currentPhase.getPhaseType() == gameState) {
            if (gameFrame.isWaiting() && gameFrame.isEmpty()) {
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
                previousPhase = currentPhase;
                currentPhase = availablePhases.get(gameState);

                if (currentPhase == null) {
                    GameCore.getErrorManager().newError(new GameError(gameFrame, ErrorType.UNKNOWN, null), true);
                    stop();
                }

                if (previousPhase != null) {
                    previousPhase.reset();
                }
                break;
            }
            case CUSTOM: {
                final var iterator = availableCustomPhases.iterator();
                if (iterator.hasNext()) {
                    previousPhase = currentPhase;
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

    /**
     * Stops whole GameCycle
     */
    @Override
    public void stop() {
        kickAllPlayers();

        gameFrame.getGameWorld().regenerate();

        super.stop();
        currentPhase = null;
    }

    /**
     * Method for game-ticking
     */
    private void tick() {
        GameCore.fireEvent(new SGameTickEvent(gameFrame, currentPhase));
        gameFrame.getPlaceholderParser().reload();
        gameFrame.updateScoreboards();

        try {
            Preconditions.checkNotNull(currentPhase, "Current phase is null, contact developer!").tick();
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
