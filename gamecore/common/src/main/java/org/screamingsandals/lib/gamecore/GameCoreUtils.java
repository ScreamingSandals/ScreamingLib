package org.screamingsandals.lib.gamecore;

import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.gamecore.entity.EntityManager;
import org.screamingsandals.lib.gamecore.listeners.ClickableEntityListener;
import org.screamingsandals.lib.gamecore.listeners.PlayerListener;
import org.screamingsandals.lib.gamecore.listeners.WorldListener;
import org.screamingsandals.lib.gamecore.players.GamePlayer;
import org.screamingsandals.lib.gamecore.players.PlayerManager;
import org.screamingsandals.lib.gamecore.region.Region;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.plugin.ServiceManager;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;
import org.screamingsandals.lib.world.BlockDataMapper;
import org.screamingsandals.lib.world.BlockMapper;
import org.screamingsandals.lib.world.state.BlockStateMapper;

import java.util.function.Supplier;

@AbstractService
@ServiceDependencies(dependsOn = {
        EntityMapper.class,
        PlayerMapper.class,
        ItemFactory.class,
        Tasker.class,
        EventManager.class,
        BlockMapper.class,
        BlockDataMapper.class,
        BlockStateMapper.class,
        WorldListener.class,
        PlayerListener.class,
        ClickableEntityListener.class,
        EntityManager.class
})
public abstract class GameCoreUtils {
    private static GameCoreUtils gameCoreUtils;

    public static void init(Supplier<GameCoreUtils> supplier) {
        if (gameCoreUtils != null) {
            throw new UnsupportedOperationException("GameCoreUtils is already initialized!");
        }
        gameCoreUtils = supplier.get();
    }

    public static boolean isInitialized() {
        return gameCoreUtils != null;
    }

    public static Region getNewRegion() {
        if (gameCoreUtils == null) {
            throw new UnsupportedOperationException("GameCoreUtils is not initialized yet!");
        }
        return gameCoreUtils.getNewRegion0();
    }

    protected abstract Region getNewRegion0();

    public static <G extends Game<G, P, ?>, P extends GamePlayer<P, G>> boolean performFakeDeath(P gamePlayer) {
        if (gameCoreUtils == null) {
            throw new UnsupportedOperationException("GameCoreUtils is not initialized yet!");
        }
        return gameCoreUtils.performFakeDeath0(gamePlayer);
    }

    protected <G extends Game<G, P, ?>, P extends GamePlayer<P, G>> boolean performFakeDeath0(P gamePlayer) {
        return false;
    }

    @SuppressWarnings("unchecked")
    public static <P extends GamePlayer<P, G>, G extends Game<G, P, GS>, GS extends GameState> GameManager<G, P, GS> getGameManager() {
        return (GameManager<G, P, GS>) ServiceManager.get(GameManager.class);
    }

    @SuppressWarnings("unchecked")
    public static <P extends GamePlayer<P, G>, G extends Game<G, P, ?>> PlayerManager<P, G> getPlayerManager() {
        return (PlayerManager<P, G>) ServiceManager.get(PlayerManager.class);
    }

}
