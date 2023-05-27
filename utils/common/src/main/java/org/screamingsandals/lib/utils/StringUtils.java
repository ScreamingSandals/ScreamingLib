package org.screamingsandals.lib.utils;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class StringUtils {
    public static @NotNull String snakeToCamel(@NotNull String input) {
        var builder = new StringBuilder();
        var length = input.length();

        var nextUppercase = false;

        for (var i = 0; i < length; i++) {
            var c = input.charAt(i);
            if (c == '_') {
                nextUppercase = true;
                continue;
            }
            builder.append(nextUppercase ? Character.toUpperCase(c) : c);

        }

        return builder.toString();
    }

    public static @NotNull String camelToSnake(@NotNull String input) {
        var builder = new StringBuilder();
        var length = input.length();

        for (var i = 0; i < length; i++) {
            var c = input.charAt(i);
            if (Character.isUpperCase(c)) {
                builder.append('_').append(Character.toLowerCase(c));
            } else {
                builder.append(c);
            }
        }

        return builder.toString();
    }
}
