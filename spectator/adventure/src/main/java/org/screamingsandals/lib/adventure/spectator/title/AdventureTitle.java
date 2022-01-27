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

package org.screamingsandals.lib.adventure.spectator.title;

import net.kyori.adventure.title.Title;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.adventure.spectator.AdventureBackend;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.time.Duration;

public class AdventureTitle extends BasicWrapper<Title> implements org.screamingsandals.lib.spectator.title.Title {
    public AdventureTitle(Title wrappedObject) {
        super(wrappedObject);
    }

    @Override
    @NotNull
    public Duration fadeIn() {
        var times = wrappedObject.times();
        return times == null ? Title.DEFAULT_TIMES.fadeIn() : times.fadeIn();
    }

    @Override
    @NotNull
    public Duration stay() {
        var times = wrappedObject.times();
        return times == null ? Title.DEFAULT_TIMES.stay() : times.stay();
    }

    @Override
    @NotNull
    public Duration fadeOut() {
        var times = wrappedObject.times();
        return times == null ? Title.DEFAULT_TIMES.fadeOut() : times.fadeOut();
    }

    @Override
    @NotNull
    public Component title() {
        return AdventureBackend.wrapComponent(wrappedObject.title());
    }

    @Override
    @NotNull
    public Component subtitle() {
        return AdventureBackend.wrapComponent(wrappedObject.subtitle());
    }

    public static class AdventureTitleBuilder implements org.screamingsandals.lib.spectator.title.Title.Builder {
        private Component title = Component.empty();
        private Component subtitle = Component.empty();
        @Nullable
        private Duration fadeIn;
        @Nullable
        private Duration stay;
        @Nullable
        private Duration fadeOut;

        @Override
        @NotNull
        public Builder title(@NotNull Component title) {
            this.title = title;
            return this;
        }

        @Override
        @NotNull
        public Builder subtitle(@NotNull Component subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        @Override
        @NotNull
        public Builder fadeIn(@Nullable Duration fadeIn) {
            this.fadeIn = fadeIn;
            return this;
        }

        @Override
        @NotNull
        public Builder stay(@Nullable Duration stay) {
            this.stay = stay;
            return this;
        }

        @Override
        @NotNull
        public Builder fadeOut(@Nullable Duration fadeOut) {
            this.fadeOut = fadeOut;
            return this;
        }

        @Override
        @NotNull
        public org.screamingsandals.lib.spectator.title.Title build() {
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
