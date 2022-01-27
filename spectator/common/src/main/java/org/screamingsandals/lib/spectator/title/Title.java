/*
 * Copyright 2022 ScreamingSandals
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
    static Title title(@NotNull Component title, @NotNull Component subtitle) {
        return builder().title(title).subtitle(subtitle).build();
    }

    static Title title(@NotNull Component title, @NotNull Component subtitle, @NotNull TimesProvider times) {
        return builder().title(title).subtitle(subtitle).times(times).build();
    }

    static Title title(@NotNull Component title, @NotNull Component subtitle, @Nullable Duration fadeIn, @Nullable Duration stay, @Nullable Duration fadeOut) {
        return builder().title(title).subtitle(subtitle).times(fadeIn, stay, fadeOut).build();
    }

    static Title title(@NotNull Component title, @NotNull Component subtitle, long fadeIn, long stay, long fadeOut) {
        return builder().title(title).subtitle(subtitle).times(fadeIn, stay, fadeOut).build();
    }

    @NotNull
    static Title.Builder builder() {
        return Spectator.getBackend().title();
    }

    @NotNull
    Component title();

    @NotNull
    Component subtitle();

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

        @NotNull
        @Contract("_ -> this")
        default Builder fadeIn(long ticks) {
            return fadeIn(Duration.ofMillis(ticks * 20));
        }

        @NotNull
        @Contract("_ -> this")
        Builder stay(@Nullable Duration stay);

        @NotNull
        @Contract("_ -> this")
        default Builder stay(long ticks) {
            return stay(Duration.ofMillis(ticks * 20));
        }

        @NotNull
        @Contract("_ -> this")
        Builder fadeOut(@Nullable Duration fadeOut);

        @NotNull
        @Contract("_ -> this")
        default Builder fadeOut(long ticks) {
            return fadeOut(Duration.ofMillis(ticks * 20));
        }

        @NotNull
        @Contract("_ -> this")
        default Builder times(@NotNull TimesProvider times) {
            fadeIn(times.fadeIn());
            stay(times.stay());
            fadeOut(times.fadeOut());
            return this;
        }

        @NotNull
        @Contract("_, _, _ -> this")
        default Builder times(@Nullable Duration fadeIn, @Nullable Duration stay, @Nullable Duration fadeOut) {
            fadeIn(fadeIn);
            stay(stay);
            fadeOut(fadeOut);
            return this;
        }

        @NotNull
        @Contract("_, _, _ -> this")
        default Builder times(long fadeIn, long stay, long fadeOut) {
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
