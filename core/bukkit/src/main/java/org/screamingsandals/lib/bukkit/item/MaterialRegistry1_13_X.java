package org.screamingsandals.lib.bukkit.item;

import com.google.common.collect.ImmutableMap;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Use {@link org.bukkit.Registry#MATERIAL} for 1.14+, this class is only for flattening pre-Bukkit-Registry versions: 1.13/1.13.1/1.13.2
 */
public final class MaterialRegistry1_13_X {
    private final @NotNull Map<NamespacedKey, Material> map;

    private static @Nullable MaterialRegistry1_13_X instance;

    public static @NotNull MaterialRegistry1_13_X getInstance() {
        if (instance == null) {
            instance = new MaterialRegistry1_13_X();
        }
        return instance;
    }

    private MaterialRegistry1_13_X() {
        ImmutableMap.Builder<NamespacedKey, Material> builder = ImmutableMap.builder();

        for (var entry : Material.values()) {
            if (!entry.isLegacy()) {
                builder.put(entry.getKey(), entry);
            }
        }

        map = builder.build();
    }

    public @Nullable Material get(@NotNull NamespacedKey key) {
        return map.get(key);
    }
}
