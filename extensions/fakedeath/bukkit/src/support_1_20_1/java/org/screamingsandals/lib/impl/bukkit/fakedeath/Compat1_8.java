package org.screamingsandals.lib.impl.bukkit.fakedeath;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@UtilityClass
public class Compat1_8 {
    @SuppressWarnings("deprecation")
    public static @NotNull PlayerDeathEvent construct(@NotNull Player player, @NotNull List<@NotNull ItemStack> drops, @Nullable String message) {
        return new PlayerDeathEvent(player, drops, player.getTotalExperience(), 0, message);
    }
}
