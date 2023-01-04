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

package org.screamingsandals.lib.placeholders.hooks;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.sender.MultiPlatformOfflinePlayer;

import java.util.regex.Pattern;

public abstract class AbstractPAPILikePlaceholder implements Hook {
    private static final @NotNull Pattern pattern = Pattern.compile("%((?<identifier>[a-zA-Z0-9]+)_)(?<parameters>[^%]+)%");

    // definitely not from:
    // https://github.com/PlaceholderAPI/PlaceholderAPI/blob/ce18d3b597b690d77a2cc485a9674b5096cdf14d/src/main/java/me/clip/placeholderapi/replacer/RegexReplacer.java
    @Override
    public @NotNull String resolveString(@Nullable MultiPlatformOfflinePlayer player, @NotNull String message) {
        final var matcher = pattern.matcher(message);
        if (!matcher.find()) {
            return message;
        }

        final StringBuilder builder = new StringBuilder();

        do {
            final String identifier = matcher.group("identifier");
            final String parameters = matcher.group("parameters");

            if (!has(identifier)) {
                continue;
            }

            var requested = resolve(player, identifier, parameters);
            matcher.appendReplacement(builder, requested != null ? requested : matcher.group(0));
        }
        while (matcher.find());

        return matcher.appendTail(builder).toString();
    }

    protected abstract boolean has(@NotNull String identifier);

    protected abstract @Nullable String resolve(@Nullable MultiPlatformOfflinePlayer player, @NotNull String identifier, @NotNull String parameters);
}
