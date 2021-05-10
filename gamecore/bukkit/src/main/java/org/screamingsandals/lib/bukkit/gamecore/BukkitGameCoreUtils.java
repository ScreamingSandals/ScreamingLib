package org.screamingsandals.lib.bukkit.gamecore;

import lombok.Getter;
import org.screamingsandals.lib.bukkit.gamecore.region.FlatteningRegion;
import org.screamingsandals.lib.bukkit.gamecore.region.LegacyRegion;
import org.screamingsandals.lib.bukkit.gamecore.utils.BukkitFakeDeath;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.gamecore.Game;
import org.screamingsandals.lib.gamecore.GameCoreUtils;
import org.screamingsandals.lib.gamecore.players.GamePlayer;
import org.screamingsandals.lib.gamecore.region.Region;
import org.screamingsandals.lib.utils.annotations.Service;

@Service
public class BukkitGameCoreUtils extends GameCoreUtils {
    @Getter
    private static final boolean legacy = !Version.isVersion(1, 13);

    public static void init() {
        GameCoreUtils.init(BukkitGameCoreUtils::new);
    }

    @Override
    protected Region getNewRegion0() {
        return legacy ? new LegacyRegion() : new FlatteningRegion();
    }

    @Override
    protected <G extends Game<G, P, ?>, P extends GamePlayer<P, G>> boolean performFakeDeath0(P gamePlayer) {
        try {
            new BukkitFakeDeath<G, P>().die(gamePlayer);
            return true;
        } catch (Throwable t) {
            return false;
        }
    }
}
