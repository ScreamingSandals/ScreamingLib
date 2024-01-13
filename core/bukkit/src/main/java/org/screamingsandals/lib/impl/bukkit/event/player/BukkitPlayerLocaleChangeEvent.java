/*
 * Copyright 2024 ScreamingSandals
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

package org.screamingsandals.lib.impl.bukkit.event.player;

import lombok.*;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bukkit.entity.BukkitPlayer;
import org.screamingsandals.lib.event.player.PlayerLocaleChangeEvent;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.Locale;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class BukkitPlayerLocaleChangeEvent implements PlayerLocaleChangeEvent {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull org.bukkit.event.player.PlayerLocaleChangeEvent event;

    // Internal cache
    private @Nullable Player player;
    private @Nullable Locale locale;

    @Override
    public @NotNull Player player() {
        if (player == null) {
            player = new BukkitPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public @NotNull Locale locale() {
        if (locale == null) {
            if (Reflect.hasMethod(event, "locale")) {
                locale = event.locale(); // java.util.Locale is not an adventure thing so we can
            } else {
                locale = parseLocale(event.getLocale());
            }
        }
        return locale;
    }

    /**
     * From Adventure
     */
    private static @Nullable Locale parseLocale(final @NotNull String string) {
        final String[] segments = string.split("_", 3); // language_country_variant
        final int length = segments.length;
        if (length == 1) {
            return new Locale(string); // language
        } else if (length == 2) {
            return new Locale(segments[0], segments[1]); // language + country
        } else if (length == 3) {
            return new Locale(segments[0], segments[1], segments[2]); // language + country + variant
        }
        return null;
    }
}
