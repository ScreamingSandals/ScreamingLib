package org.screamingsandals.lib.gamecore.core.cycle;

import com.google.common.base.Preconditions;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.debug.Debug;
import org.screamingsandals.lib.gamecore.GameCore;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.core.GameState;
import org.screamingsandals.lib.gamecore.core.phase.GamePhase;
import org.screamingsandals.lib.gamecore.events.core.game.SGameTickEvent;
import org.screamingsandals.lib.gamecore.player.GamePlayer;
import org.screamingsandals.lib.tasker.BaseTask;

import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class GameCycle extends BaseTask {
    private final GameFrame gameFrame;
    private GamePhase currentPhase;
    private Map<GameState, GamePhase> gamePhases = new HashMap<>();
    private List<GamePhase> customPhases = new LinkedList<>();
    private Type type;

    @Override
    public void run() {
        final GameState gameState = gameFrame.getActiveState();
        if (currentPhase != null && currentPhase.getPhaseType() == gameState) {
            if (gameState == GameState.WAITING && gameFrame.getPlayersInGame().size() == 0) {
                //Don't tick if players in game = 0, and we are waiting
                return;
            }

            tick();
        }

        switch (gameState) {
            case LOADING: {
                gameFrame.setGameState(GameState.WAITING);
                currentPhase = gamePhases.get(GameState.LOADING);
                break;
            }
            case WAITING: {
                currentPhase = gamePhases.get(GameState.WAITING);
                break;
            }
            case PRE_GAME_COUNTDOWN: {
                currentPhase = gamePhases.get(GameState.PRE_GAME_COUNTDOWN);
                break;
            }
            case IN_GAME: {
                currentPhase = gamePhases.get(GameState.IN_GAME);
                break;
            }
            case DEATHMATCH: {
                currentPhase = gamePhases.get(GameState.DEATHMATCH);
                break;
            }
            case AFTER_GAME_COUNTDOWN: {
                currentPhase = gamePhases.get(GameState.AFTER_GAME_COUNTDOWN);
            }
            case RESTART: {
                currentPhase = gamePhases.get(GameState.RESTART);
                break;
            }
            case MAINTENANCE: {
                currentPhase = gamePhases.get(GameState.MAINTENANCE);
            }
            case CUSTOM: {
                final Iterator<GamePhase> iterator = customPhases.iterator();
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

    public void stop() {
        kickAllPlayers();

        stop();

        currentPhase = null;
    }

    private void tick() {
        GameCore.fireEvent(new SGameTickEvent(gameFrame, currentPhase));
        Preconditions.checkNotNull(currentPhase).tick();
    }

    public void kickAllPlayers() {

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

    public enum Type {
        SINGLE_GAME_BUNGEE,
        MULTI_GAME_BUNGEE,
        MULTI_GAME
    }
}
