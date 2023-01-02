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

package org.screamingsandals.lib.spectator.title;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.Spectator;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.Wrapper;

import java.time.Duration;

// TODO: Support for AudienceComponentLike
public interface Title extends Wrapper, RawValueHolder, TimesProvider {
    @Contract(value = "_, _ -> new", pure = true)
    static @NotNull Title title(@NotNull Component title, @NotNull Component subtitle) {
        return builder().title(title).subtitle(subtitle).build();
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    static @NotNull Title title(@NotNull Component title, @NotNull Component subtitle, @Nullable TimesProvider times) {
        return builder().title(title).subtitle(subtitle).times(times).build();
    }

    @Contract(value = "_, _, _, _, _ -> new", pure = true)
    static @NotNull Title title(@NotNull Component title, @NotNull Component subtitle, @Nullable Duration fadeIn, @Nullable Duration stay, @Nullable Duration fadeOut) {
        return builder().title(title).subtitle(subtitle).times(fadeIn, stay, fadeOut).build();
    }

    @Contract(value = "_, _, _, _, _ -> new", pure = true)
    static @NotNull Title title(@NotNull Component title, @NotNull Component subtitle, long fadeIn, long stay, long fadeOut) {
        return builder().title(title).subtitle(subtitle).times(fadeIn, stay, fadeOut).build();
    }

    @Contract(value = "-> new", pure = true)
    static @NotNull Title.Builder builder() {
        return Spectator.getBackend().title();
    }

    @NotNull
    Component title();

    @Contract(pure = true)
    @NotNull
    Title withTitle(@NotNull Component title);

    @NotNull
    Component subtitle();

    @Contract(pure = true)
    @NotNull
    Title withSubtitle(@NotNull Component subtitle);

    @Contract(pure = true)
    @NotNull
    Title withTimes(@NotNull TimesProvider times);

    @Contract(pure = true)
    @NotNull
    Title withTimes(@Nullable Duration fadeIn, @Nullable Duration stay, @Nullable Duration fadeOut);

    @Contract(pure = true)
    @NotNull
    Title withTimes(long fadeIn, long stay, long fadeOut);

    @Contract(pure = true)
    @NotNull
    Title withFadeIn(@Nullable Duration fadeIn);

    @Contract(pure = true)
    @NotNull
    Title withStay(@Nullable Duration stay);

    @Contract(pure = true)
    @NotNull
    Title withFadeOut(@Nullable Duration fadeOut);

    @Contract(pure = true)
    @NotNull
    Title withFadeIn(long ticks);

    @Contract(pure = true)
    @NotNull
    Title withStay(long ticks);

    @Contract(pure = true)
    @NotNull
    Title withFadeOut(long ticks);

    @Contract(value = "-> new", pure = true)
    @NotNull
    Title.Builder toBuilder();

    interface Builder {
        @NotNull
        @Contract("_ -> this")
        Builder title(@NotNull Component title);

        @NotNull
        @Contract("_ -> this")
        Builder subtitle(@NotNull Component subtitle);

        @NotNull
        @Contract("_ -> this")
        Builder fadeIn(@Nullable Duration fadeIn);

        @Contract("_ -> this")
        default @NotNull Builder fadeIn(long ticks) {
            return fadeIn(Duration.ofMillis(ticks * 50));
        }

        @NotNull
        @Contract("_ -> this")
        Builder stay(@Nullable Duration stay);

        @Contract("_ -> this")
        default @NotNull Builder stay(long ticks) {
            return stay(Duration.ofMillis(ticks * 50));
        }

        @NotNull
        @Contract("_ -> this")
        Builder fadeOut(@Nullable Duration fadeOut);

        @Contract("_ -> this")
        default @NotNull Builder fadeOut(long ticks) {
            return fadeOut(Duration.ofMillis(ticks * 50));
        }

        @Contract("_ -> this")
        default @NotNull Builder times(@Nullable TimesProvider times) {
            if (times != null) {
                fadeIn(times.fadeIn());
                stay(times.stay());
                fadeOut(times.fadeOut());
            } else {
                fadeIn(null);
                stay(null);
                fadeOut(null);
            }
            return this;
        }

        @Contract("_, _, _ -> this")
        default @NotNull Builder times(@Nullable Duration fadeIn, @Nullable Duration stay, @Nullable Duration fadeOut) {
            fadeIn(fadeIn);
            stay(stay);
            fadeOut(fadeOut);
            return this;
        }

        @Contract("_, _, _ -> this")
        default @NotNull Builder times(long fadeIn, long stay, long fadeOut) {
            fadeIn(fadeIn);
            stay(stay);
            fadeOut(fadeOut);
            return this;
        }

        @NotNull
        @Contract(value = "-> new", pure = true)
        Title build();
    }
}
