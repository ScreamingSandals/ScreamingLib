/*
 * Copyright 2025 ScreamingSandals
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
import org.screamingsandals.lib.impl.adventure.spectator.AdventureFeature;
import org.screamingsandals.lib.impl.adventure.spectator.event.click.PayloadConverter;
import org.screamingsandals.lib.spectator.event.ClickEvent;
import org.screamingsandals.lib.spectator.event.click.Payload;
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
        if (AdventureFeature.CLICK_EVENT_PAYLOAD.isSupported()) {
            return new AdventureClickEvent(PayloadConverter.withAction(
                    net.kyori.adventure.text.event.ClickEvent.Action.valueOf(action.name()),
                    wrappedObject
            ));
        } else {
            return new AdventureClickEvent(net.kyori.adventure.text.event.ClickEvent.clickEvent(
                    net.kyori.adventure.text.event.ClickEvent.Action.valueOf(action.name()),
                    wrappedObject.value()
            ));
        }
    }

    @Override
    public @NotNull Payload payload() {
        if (AdventureFeature.CLICK_EVENT_PAYLOAD.isSupported()) {
            return PayloadConverter.convertPayload(wrappedObject);
        } else {
            return new Payload.Text.Default(wrappedObject.value());
        }
    }

    @Override
    public @NotNull ClickEvent withPayload(@NotNull Payload payload) {
        if (AdventureFeature.CLICK_EVENT_PAYLOAD.isSupported()) {
            return new AdventureClickEvent(PayloadConverter.withPayload(wrappedObject.action(), payload));
        } else {
            String value;
            if (payload instanceof Payload.Text) {
                value = ((Payload.Text) payload).text();
            } else if (payload instanceof Payload.Int) {
                value = String.valueOf(((Payload.Int) payload).number());
            } else {
                throw new IllegalArgumentException("Invalid payload type for action " + wrappedObject.action().name() + ": " + payload.getClass());
            }
            return new AdventureClickEvent(net.kyori.adventure.text.event.ClickEvent.clickEvent(wrappedObject.action(), value));
        }
    }

    @Override
    public ClickEvent.@NotNull Builder toBuilder() {
        return new AdventureClickEventBuilder(action(), payload());
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
        private @Nullable Payload payload;

        @Override
        public @NotNull ClickEvent build() {
            Preconditions.checkNotNull(action, "Action is not specified!");
            Preconditions.checkNotNull(payload, "Payload is not specified!");
            if (AdventureFeature.CLICK_EVENT_PAYLOAD.isSupported()) {
                return new AdventureClickEvent(PayloadConverter.withPayload(
                        net.kyori.adventure.text.event.ClickEvent.Action.valueOf(action.name()),
                        payload
                ));
            } else {
                String value;
                if (payload instanceof Payload.Text) {
                    value = ((Payload.Text) payload).text();
                } else if (payload instanceof Payload.Int) {
                    value = String.valueOf(((Payload.Int) payload).number());
                } else {
                    throw new IllegalArgumentException("Invalid payload type for action " + action.name() + ": " + payload.getClass());
                }
                return new AdventureClickEvent(net.kyori.adventure.text.event.ClickEvent.clickEvent(
                        net.kyori.adventure.text.event.ClickEvent.Action.valueOf(action.name()),
                        value
                ));
            }
        }
    }
}
