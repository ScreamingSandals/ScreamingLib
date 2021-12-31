package org.screamingsandals.lib.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

@UtilityClass
public class StringUtils {
    public String capitalizeFirst(String s) {
        return s.substring(0, 1).toUpperCase(Locale.ROOT) + s.substring(1);
    }

    public interface Case {
        Case LOWER_CAMEL = new LowerCamelCase();
        Case SNAKE = new SnakeCase();

        String toLowerCamel(String s);
        String toSnake(String s);
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    static class LowerCamelCase implements Case {
        @Override
        public String toLowerCamel(String s) {
            return s;
        }

        @Override
        public String toSnake(String s) {
            final StringBuilder builder = new StringBuilder();
            for (char c : s.toCharArray()) {
                if (Character.isUpperCase(c)) {
                    builder.append("_");
                }
                builder.append(Character.toLowerCase(c));
            }
            return builder.toString();
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    static class SnakeCase implements Case {
        @Override
        public String toLowerCamel(String s) {
            final String[] split = s.split("_");
            if (split.length == 1) {
                return s;
            }
            return split[0] + Arrays.stream(Arrays.copyOfRange(split, 1, split.length))
                    .map(StringUtils::capitalizeFirst)
                    .collect(Collectors.joining());
        }

        @Override
        public String toSnake(String s) {
            return s;
        }
    }
}
