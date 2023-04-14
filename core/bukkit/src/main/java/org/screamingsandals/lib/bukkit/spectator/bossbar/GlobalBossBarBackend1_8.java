package org.screamingsandals.lib.bukkit.spectator.bossbar;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class GlobalBossBarBackend1_8 {
    @Getter
    @Setter
    private static BukkitBossBar1_8.@NotNull Backend backend = BukkitBossBar1_8.Backend.WITHER;
}
