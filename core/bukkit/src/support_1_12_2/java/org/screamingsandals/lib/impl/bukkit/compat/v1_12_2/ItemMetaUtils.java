package org.screamingsandals.lib.impl.bukkit.compat.v1_12_2;

import lombok.experimental.UtilityClass;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class ItemMetaUtils {
    @SuppressWarnings("deprecation")
    public static void spigotSetUnbreakable(@NotNull ItemMeta itemMeta, boolean value) {
        itemMeta.spigot().setUnbreakable(value);
    }
}
