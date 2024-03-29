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

package org.screamingsandals.lib.impl.adventure.spectator.event;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.adventure.spectator.AdventureBackend;
import org.screamingsandals.lib.spectator.event.ClickEvent;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.Preconditions;

public class AdventureClickEvent extends BasicWrapper<net.kyori.adventure.text.event.ClickEvent> implements ClickEvent {
    public AdventureClickEvent(net.kyori.adventure.text.event.@NotNull ClickEvent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull Action action() {
        try {
            return Action.valueOf(wrappedObject.action().name());
        } catch (Throwable ignored) {
            return Action.OPEN_URL; // HOW??
        }
    }

    @Override
    public @NotNull ClickEvent withAction(@NotNull Action action) {
        return new AdventureClickEvent(net.kyori.adventure.text.event.ClickEvent.clickEvent(
                net.kyori.adventure.text.event.ClickEvent.Action.valueOf(action.name()),
                wrappedObject.value()
        ));
    }

    @Override
    public @NotNull String value() {
        return wrappedObject.value();
    }

    @Override
    public @NotNull ClickEvent withValue(@NotNull String value) {
        return new AdventureClickEvent(net.kyori.adventure.text.event.ClickEvent.clickEvent(wrappedObject.action(), value));
    }

    @Override
    public ClickEvent.@NotNull Builder toBuilder() {
        return new AdventureClickEventBuilder(action(), value());
    }

    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        try {
            return super.as(type);
        } catch (Throwable ignored) {
            return AdventureBackend.getAdditionalClickEventConverter().convert(this, type);
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(fluent = true, chain = true)
    @Setter
    public static class AdventureClickEventBuilder implements ClickEvent.Builder {
        private @NotNull Action action = Action.OPEN_URL;
        private @Nullable String value;

        @Override
        public @NotNull ClickEvent build() {
            Preconditions.checkNotNull(action, "Action is not specified!");
            Preconditions.checkNotNull(value, "Value is not specified!");
            return new AdventureClickEvent(net.kyori.adventure.text.event.ClickEvent.clickEvent(net.kyori.adventure.text.event.ClickEvent.Action.valueOf(action.name()), value));
        }
    }
}
