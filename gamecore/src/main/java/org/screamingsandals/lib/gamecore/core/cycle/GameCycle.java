package org.screamingsandals.lib.gamecore.core.cycle;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.debug.Debug;
import org.screamingsandals.lib.gamecore.GameCore;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.core.GameState;
import org.screamingsandals.lib.gamecore.core.phase.CustomGamePhase;
import org.screamingsandals.lib.gamecore.core.phase.GamePhase;
import org.screamingsandals.lib.gamecore.error.ErrorType;
import org.screamingsandals.lib.gamecore.error.GameError;
import org.screamingsandals.lib.gamecore.events.core.game.SGamePreTickEvent;
import org.screamingsandals.lib.gamecore.events.core.game.SGameTickEvent;
import org.screamingsandals.lib.gamecore.player.GamePlayer;
import org.screamingsandals.lib.tasker.BaseTask;
import org.screamingsandals.lib.tasker.TaskerTime;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = false)
@Data
public abstract class GameCycle {
    protected final transient GameFrame gameFrame;
    protected CycleType cycleType;

    protected Map<GameState, GamePhase> availablePhases = new LinkedHashMap<>();
    protected List<CustomGamePhase> availableCustomPhases = new LinkedList<>();
    protected List<CustomGamePhase> previousCustomPhases = new LinkedList<>();

    protected GamePhase currentPhase;
    protected GamePhase previousPhase;

    protected BaseTask tickTask;

    public GameCycle(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        prepare();
    }

    /**
     * Initialize ticking task
     * You can always supply your own task if you want
     */
    protected void prepare() {
        tickTask = new BaseTask() {
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

                            previousCustomPhases.add((CustomGamePhase) currentPhase);
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

                gameFrame.getGameWorld().regenerate();
                gameFrame.getVisualsManager().hideAll();

                super.stop();
                currentPhase = null;
            }
        };
    }

    /**
     * Fired right before the game tick is happening.
     *
     * @return if return value is false, game tick won't happen
     */
    protected boolean preTick() {
        if (!GameCore.fireEvent(new SGamePreTickEvent(gameFrame, this, currentPhase))) {
            return false;
        }

        return currentPhase.preTick();
    }

    /**
     * Method for game-ticking
     */
    protected void tick() {
        if (currentPhase == null) {
            GameCore.getErrorManager().newError(new GameError(gameFrame, ErrorType.GAME_CYCLE_IS_NULL, null), true);
            tickTask.stop();
            return;
        }

        if (!preTick()) {
            Debug.info("preTick is false, return!");
            return;
        }

        if (!GameCore.fireEvent(new SGameTickEvent(gameFrame, this, currentPhase))) {
            return;
        }

        currentPhase.tick();

        gameFrame.getPlaceholderParser().update();
        gameFrame.getVisualsManager().update();
    }

    /**
     *
     */
    public void switchPhase(GameState newState) {
        gameFrame.setActiveState(newState);

        gameFrame.getPlaceholderParser().update();
        gameFrame.getVisualsManager().update();
    }

    public void start() {
        if (tickTask == null) {
            return;
        }

        tickTask.runTaskRepeater(0, 1, TaskerTime.SECONDS);
    }

    public void stop() {
        if (tickTask == null) {
            return;
        }

        tickTask.stop();
    }

    public boolean hasStopped() {
        if (tickTask == null) {
            return false;
        }

        return tickTask.hasStopped();
    }

    public void kickAllPlayers() {
        if (gameFrame.getPlayersInGame().isEmpty()) {
            return;
        }
        gameFrame.getPlayersInGame().forEach(this::kickPlayer);
        gameFrame.getSpectators().forEach(this::kickPlayer);
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

    public void addCustomPhase(CustomGamePhase customGamePhase) {
        availableCustomPhases.add(customGamePhase);
    }

    public boolean isRunning() {
        return currentPhase != null;
    }
}
