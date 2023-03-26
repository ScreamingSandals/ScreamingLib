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

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Collectors;

@UtilityClass
public class StringUtils {
    public @NotNull String capitalizeFirst(@NotNull String s) {
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    public @NotNull String fromLowerCamelToSnake(@NotNull String s) {
        final StringBuilder builder = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (Character.isUpperCase(c)) {
                builder.append("_");
            }
            builder.append(Character.toLowerCase(c));
        }
        return builder.toString();
    }

    public @NotNull String fromSnakeToLowerCamel(@NotNull String s) {
        final String[] split = s.split("_");
        if (split.length == 1) {
            return s;
        }
        return split[0] + Arrays.stream(Arrays.copyOfRange(split, 1, split.length))
                .map(StringUtils::capitalizeFirst)
                .collect(Collectors.joining());
    }
}
