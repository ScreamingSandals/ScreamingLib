/*
 * Copyright 2023 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.stream.Collectors;

@UtilityClass
public class StringUtils {
    public String capitalizeFirst(String s) {
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
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
