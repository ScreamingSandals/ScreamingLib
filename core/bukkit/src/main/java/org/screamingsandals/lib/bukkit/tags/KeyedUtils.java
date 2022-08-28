package org.screamingsandals.lib.bukkit.tags;

import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

// To trick plugin-class-loader in legacy, all methods here are not in their respective Mapping class, that's also why we can't use generics here properly
public class KeyedUtils {
    @SuppressWarnings("unchecked")
    public static <T> boolean isTagged(@NotNull String registry, @NotNull NamespacedKey key, @NotNull Class<T> type, @NotNull T object) {
        var bukkitTag = Bukkit.getTag(registry, key, (Class<Keyed>) type);
        if (bukkitTag != null) {
            return bukkitTag.isTagged((Keyed) object);
        }
        return false;
    }
}
