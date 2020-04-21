package org.screamingsandals.gamecore.core.cycle;

import com.google.common.base.Preconditions;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.gamecore.GameCore;
import org.screamingsandals.gamecore.core.GameFrame;
import org.screamingsandals.gamecore.core.GameState;
import org.screamingsandals.gamecore.core.phase.GamePhase;
import org.screamingsandals.gamecore.events.core.game.GameCoreGameTickEvent;
import org.screamingsandals.gamecore.player.GamePlayer;

import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class GameCycle extends BukkitRunnable {
    private final GameFrame gameFrame;
    private GamePhase currentPhase;
    private Map<GameState, GamePhase> gamePhases = new HashMap<>();
    private List<GamePhase> customPhases = new LinkedList<>();

    @Override
    public void run() {
        final GameState gameState = gameFrame.getActiveState();
        if (currentPhase != null && currentPhase.getPhaseType() == gameState) {
            GameCore.fireEvent(new GameCoreGameTickEvent(gameFrame, currentPhase));
            currentPhase.tick();
            return;
        }

        switch (gameState) {
            case LOADING: {
                gameFrame.setActiveState(GameState.WAITING);
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
                cancel();
                return;
            }
        }

        GameCore.fireEvent(new GameCoreGameTickEvent(gameFrame, currentPhase));
        Preconditions.checkNotNull(currentPhase).tick();
    }

    public void stop() {
        kickAllPlayers();

        cancel();
    }

    public void kickAllPlayers() {

    }

    public void kickPlayer(GamePlayer gamePlayer) {

    }
}
