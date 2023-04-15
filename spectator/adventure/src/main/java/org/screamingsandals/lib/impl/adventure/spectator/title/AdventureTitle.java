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

package org.screamingsandals.lib.impl.adventure.spectator.title;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.kyori.adventure.title.Title;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.adventure.spectator.AdventureBackend;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.title.TimesProvider;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.time.Duration;

public class AdventureTitle extends BasicWrapper<Title> implements org.screamingsandals.lib.spectator.title.Title {
    public AdventureTitle(@NotNull Title wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull Duration fadeIn() {
        var times = wrappedObject.times();
        return times == null ? Title.DEFAULT_TIMES.fadeIn() : times.fadeIn();
    }

    @Override
    public @NotNull Duration stay() {
        var times = wrappedObject.times();
        return times == null ? Title.DEFAULT_TIMES.stay() : times.stay();
    }

    @Override
    public @NotNull Duration fadeOut() {
        var times = wrappedObject.times();
        return times == null ? Title.DEFAULT_TIMES.fadeOut() : times.fadeOut();
    }

    @Override
    public @NotNull Component title() {
        return AdventureBackend.wrapComponent(wrappedObject.title());
    }

    @Override
    public org.screamingsandals.lib.spectator.title.@NotNull Title withTitle(@NotNull Component title) {
        return new AdventureTitle(Title.title(title.as(net.kyori.adventure.text.Component.class), wrappedObject.subtitle(), wrappedObject.times()));
    }

    @Override
    public @NotNull Component subtitle() {
        return AdventureBackend.wrapComponent(wrappedObject.subtitle());
    }

    @Override
    public org.screamingsandals.lib.spectator.title.@NotNull Title withSubtitle(@NotNull Component subtitle) {
        return new AdventureTitle(Title.title(wrappedObject.title(), subtitle.as(net.kyori.adventure.text.Component.class), wrappedObject.times()));
    }

    @Override
    public org.screamingsandals.lib.spectator.title.@NotNull Title withTimes(@NotNull TimesProvider times) {
        return new AdventureTitle(Title.title(wrappedObject.title(), wrappedObject.subtitle(), Title.Times.of(times.fadeIn(), times.stay(), times.fadeOut())));
    }

    @Override
    public org.screamingsandals.lib.spectator.title.@NotNull Title withTimes(@Nullable Duration fadeIn, @Nullable Duration stay, @Nullable Duration fadeOut) {
        return new AdventureTitle(Title.title(wrappedObject.title(), wrappedObject.subtitle(), Title.Times.of(
                fadeIn != null ? fadeIn : Title.DEFAULT_TIMES.fadeIn(),
                stay != null ? stay : Title.DEFAULT_TIMES.stay(),
                fadeOut != null ? fadeOut : Title.DEFAULT_TIMES.fadeOut()
        )));
    }

    @Override
    public org.screamingsandals.lib.spectator.title.@NotNull Title withTimes(long fadeIn, long stay, long fadeOut) {
        return new AdventureTitle(Title.title(wrappedObject.title(), wrappedObject.subtitle(), Title.Times.of(
                Duration.ofMillis(fadeIn * 50),
                Duration.ofMillis(stay * 50),
                Duration.ofMillis(fadeOut * 50)
        )));
    }

    @Override
    public org.screamingsandals.lib.spectator.title.@NotNull Title withFadeIn(@Nullable Duration fadeIn) {
        return new AdventureTitle(Title.title(wrappedObject.title(), wrappedObject.subtitle(), Title.Times.of(
                fadeIn != null ? fadeIn : Title.DEFAULT_TIMES.fadeIn(),
                stay(),
                fadeOut()
        )));
    }

    @Override
    public org.screamingsandals.lib.spectator.title.@NotNull Title withStay(@Nullable Duration stay) {
        return new AdventureTitle(Title.title(wrappedObject.title(), wrappedObject.subtitle(), Title.Times.of(
                fadeIn(),
                stay != null ? stay : Title.DEFAULT_TIMES.stay(),
                fadeOut()
        )));
    }

    @Override
    public org.screamingsandals.lib.spectator.title.@NotNull Title withFadeOut(@Nullable Duration fadeOut) {
        return new AdventureTitle(Title.title(wrappedObject.title(), wrappedObject.subtitle(), Title.Times.of(
                fadeIn(),
                stay(),
                fadeOut != null ? fadeOut : Title.DEFAULT_TIMES.fadeOut()
        )));
    }

    @Override
    public org.screamingsandals.lib.spectator.title.@NotNull Title withFadeIn(long ticks) {
        return new AdventureTitle(Title.title(wrappedObject.title(), wrappedObject.subtitle(), Title.Times.of(
                Duration.ofMillis(ticks * 50),
                stay(),
                fadeOut()
        )));
    }

    @Override
    public org.screamingsandals.lib.spectator.title.@NotNull Title withStay(long ticks) {
        return new AdventureTitle(Title.title(wrappedObject.title(), wrappedObject.subtitle(), Title.Times.of(
                fadeIn(),
                Duration.ofMillis(ticks * 50),
                fadeOut()
        )));
    }

    @Override
    public org.screamingsandals.lib.spectator.title.@NotNull Title withFadeOut(long ticks) {
        return new AdventureTitle(Title.title(wrappedObject.title(), wrappedObject.subtitle(), Title.Times.of(
                fadeIn(),
                stay(),
                Duration.ofMillis(ticks * 50)
        )));
    }

    @Override
    public org.screamingsandals.lib.spectator.title.Title.@NotNull Builder toBuilder() {
        return new AdventureTitleBuilder(title(), subtitle(), fadeIn(), stay(), fadeOut());
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdventureTitleBuilder implements org.screamingsandals.lib.spectator.title.Title.Builder {
        private @NotNull Component title = Component.empty();
        private @NotNull Component subtitle = Component.empty();
        private @Nullable Duration fadeIn;
        private @Nullable Duration stay;
        private @Nullable Duration fadeOut;

        @Override
        public @NotNull Builder title(@NotNull Component title) {
            this.title = title;
            return this;
        }

        @Override
        public @NotNull Builder subtitle(@NotNull Component subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        @Override
        public @NotNull Builder fadeIn(@Nullable Duration fadeIn) {
            this.fadeIn = fadeIn;
            return this;
        }

        @Override
        public @NotNull Builder stay(@Nullable Duration stay) {
            this.stay = stay;
            return this;
        }

        @Override
        public @NotNull Builder fadeOut(@Nullable Duration fadeOut) {
            this.fadeOut = fadeOut;
            return this;
        }

        @Override
        public org.screamingsandals.lib.spectator.title.@NotNull Title build() {
            if (fadeIn == null && fadeOut == null && stay == null) {
                return new AdventureTitle(
                        Title.title(
                                title.as(net.kyori.adventure.text.Component.class),
                                subtitle.as(net.kyori.adventure.text.Component.class)
                        )
                );
            }
            return new AdventureTitle(
                    Title.title(
                            title.as(net.kyori.adventure.text.Component.class),
                            subtitle.as(net.kyori.adventure.text.Component.class),
                            Title.Times.of(
                                fadeIn != null ? fadeIn : Title.DEFAULT_TIMES.fadeIn(),
                                stay != null ? stay : Title.DEFAULT_TIMES.stay(),
                                fadeOut != null ? fadeOut : Title.DEFAULT_TIMES.fadeOut()
                            )
                    )
            );
        }
    }
}
