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
