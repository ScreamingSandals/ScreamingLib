package org.screamingsandals.lib.adventure.spectator.bossbar;

import net.kyori.adventure.bossbar.BossBar;
import org.screamingsandals.lib.spectator.bossbar.BossBarColor;
import org.screamingsandals.lib.spectator.bossbar.BossBarDivision;
import org.screamingsandals.lib.spectator.bossbar.BossBarFlag;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BossBarUtils {
    public static BossBarColor convertColor(BossBar.Color color) {
        try {
            return BossBarColor.valueOf(color.name());
        } catch (Throwable t) {
            return BossBarColor.PURPLE;
        }
    }

    public static BossBar.Color convertColor(BossBarColor color) {
        return net.kyori.adventure.bossbar.BossBar.Color.valueOf(color.name());
    }

    public static BossBarDivision convertDivision(BossBar.Overlay overlay) {
        try {
            return BossBarDivision.valueOf(overlay.name());
        } catch (Throwable t) {
            return BossBarDivision.NO_DIVISION;
        }
    }

    public static BossBar.Overlay convertDivision(BossBarDivision division) {
        if (division == BossBarDivision.NO_DIVISION) {
            return net.kyori.adventure.bossbar.BossBar.Overlay.PROGRESS;
        } else {
            return net.kyori.adventure.bossbar.BossBar.Overlay.valueOf(division.name());
        }
    }

    public static List<BossBarFlag> convertFlags(Set<BossBar.Flag> flags) {
        return flags.stream()
                .map(flag -> BossBarFlag.valueOf(flag.name()))
                .collect(Collectors.toUnmodifiableList());
    }

    public static Set<BossBar.Flag> convertFlags(Collection<BossBarFlag> flags) {
        return flags.stream()
                .map(bossBarFlag -> net.kyori.adventure.bossbar.BossBar.Flag.valueOf(bossBarFlag.name()))
                .collect(Collectors.toUnmodifiableSet());
    }
}
