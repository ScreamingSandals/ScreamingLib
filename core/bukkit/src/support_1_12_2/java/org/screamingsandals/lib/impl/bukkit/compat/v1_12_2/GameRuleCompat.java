package org.screamingsandals.lib.impl.bukkit.compat.v1_12_2;

import lombok.experimental.UtilityClass;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public class GameRuleCompat {
    public static <T> @Nullable T getGameRuleValue(@NotNull World world, @NotNull String gameRule) {
        var val = world.getGameRuleValue(gameRule);
        if (val == null) {
            return null;
        }
        try {
            return (T) Integer.valueOf(val);
        } catch (Throwable ignored) {
            if ("true".equalsIgnoreCase(val) || "false".equalsIgnoreCase(val)) {
                return (T) Boolean.valueOf(val);
            } else {
                return (T) val;
            }
        }
    }

    public static <T> void setGameRuleValue(@NotNull World world, @NotNull String gameRule, @NotNull T value) {
        world.setGameRuleValue(gameRule, value.toString());
    }
}
