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

package org.screamingsandals.lib.impl.bukkit.spectator.title;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.title.TimesProvider;
import org.screamingsandals.lib.spectator.title.Title;

import java.time.Duration;

@Data
@Accessors(fluent = true)
public class BukkitTitle implements Title {
    private final @NotNull Component title;
    private final @NotNull Component subtitle;
    private final @NotNull Duration fadeIn;
    private final @NotNull Duration stay;
    private final @NotNull Duration fadeOut;

    @SuppressWarnings("unchecked")
    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        if (type.isInstance(this)) {
            return (T) this;
        }
        throw new UnsupportedOperationException("Bukkit doesn't have any class for titles, just methods");
    }

    @Override
    public @NotNull Object raw() {
        throw new UnsupportedOperationException("Bukkit doesn't have any class for titles, just methods");
    }

    @Override
    public @NotNull Title withTitle(@NotNull Component title) {
        return new BukkitTitle(title, subtitle, fadeIn, stay, fadeOut);
    }

    @Override
    public @NotNull Title withSubtitle(@NotNull Component subtitle) {
        return new BukkitTitle(title, subtitle, fadeIn, stay, fadeOut);
    }

    @Override
    public @NotNull Title withTimes(@NotNull TimesProvider times) {
        return new BukkitTitle(title, subtitle, times.fadeIn(), times.stay(), times.fadeOut());
    }

    @Override
    public @NotNull Title withTimes(@Nullable Duration fadeIn, @Nullable Duration stay, @Nullable Duration fadeOut) {
        return new BukkitTitle(title, subtitle,
                fadeIn == null ? Duration.ofMillis(500) : fadeIn,
                stay == null ? Duration.ofMillis(3500) : stay,
                fadeOut == null ? Duration.ofMillis(1000) : fadeOut
        );
    }

    @Override
    public @NotNull Title withTimes(long fadeIn, long stay, long fadeOut) {
        return new BukkitTitle(title, subtitle, Duration.ofMillis(fadeIn * 50), Duration.ofMillis(stay * 50), Duration.ofMillis(fadeOut * 50));
    }

    @Override
    public @NotNull Title withFadeIn(@Nullable Duration fadeIn) {
        return new BukkitTitle(title, subtitle, fadeIn == null ? Duration.ofMillis(500) : fadeIn, stay, fadeOut);
    }

    @Override
    public @NotNull Title withStay(@Nullable Duration stay) {
        return new BukkitTitle(title, subtitle, fadeIn, stay == null ? Duration.ofMillis(3500) : stay, fadeOut);
    }

    @Override
    public @NotNull Title withFadeOut(@Nullable Duration fadeOut) {
        return new BukkitTitle(title, subtitle, fadeIn, stay, fadeOut == null ? Duration.ofMillis(1000) : fadeOut);
    }

    @Override
    public @NotNull Title withFadeIn(long ticks) {
        return new BukkitTitle(title, subtitle, Duration.ofMillis(ticks * 50), stay, fadeOut);
    }

    @Override
    public @NotNull Title withStay(long ticks) {
        return new BukkitTitle(title, subtitle, fadeIn, Duration.ofMillis(ticks * 50), fadeOut);
    }

    @Override
    public @NotNull Title withFadeOut(long ticks) {
        return new BukkitTitle(title, subtitle, fadeIn, stay, Duration.ofMillis(ticks * 50));
    }

    @Override
    public Title.@NotNull Builder toBuilder() {
        return new BukkitTitleBuilder(title, subtitle, fadeIn, stay, fadeOut);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    public static class BukkitTitleBuilder implements Title.Builder {
        private @Nullable Component title;
        private @Nullable Component subtitle;
        private @Nullable Duration fadeIn;
        private @Nullable Duration stay;
        private @Nullable Duration fadeOut;

        @Override
        public @NotNull Title build() {
            return new BukkitTitle(
                    title == null ? Component.empty() : title,
                    subtitle == null ? Component.empty() : subtitle,
                    fadeIn == null ? Duration.ofMillis(500) : fadeIn,
                    stay == null ? Duration.ofMillis(3500) : stay,
                    fadeOut == null ? Duration.ofMillis(1000) : fadeOut
            );
        }
    }
}
