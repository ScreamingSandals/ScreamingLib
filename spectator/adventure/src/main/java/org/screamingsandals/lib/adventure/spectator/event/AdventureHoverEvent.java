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

package org.screamingsandals.lib.adventure.spectator.event;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.adventure.spectator.AdventureBackend;
import org.screamingsandals.lib.adventure.spectator.event.hover.AdventureEntityContent;
import org.screamingsandals.lib.adventure.spectator.event.hover.AdventureItemContent;
import org.screamingsandals.lib.spectator.event.HoverEvent;
import org.screamingsandals.lib.spectator.event.hover.Content;
import org.screamingsandals.lib.spectator.event.hover.EntityContent;
import org.screamingsandals.lib.spectator.event.hover.ItemContent;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.Preconditions;

import java.util.Locale;
import java.util.Objects;

public class AdventureHoverEvent extends BasicWrapper<net.kyori.adventure.text.event.HoverEvent<?>> implements HoverEvent {
    public AdventureHoverEvent(net.kyori.adventure.text.event.HoverEvent<?> wrappedObject) {
        super(wrappedObject);
    }

    @Override
    @NotNull
    public Action action() {
        try {
            return Action.valueOf(Objects.requireNonNull(net.kyori.adventure.text.event.HoverEvent.Action.NAMES.key(wrappedObject.action())).toUpperCase(Locale.ROOT));
        } catch (Throwable ignored) {
            return Action.SHOW_TEXT; // HOW??
        }
    }

    @Override
    @NotNull
    public Content content() {
        if (wrappedObject.action().type() == Component.class) {
            return AdventureBackend.wrapComponent((Component) wrappedObject.value());
        } else if (wrappedObject.action().type() == net.kyori.adventure.text.event.HoverEvent.ShowItem.class) {
            return new AdventureItemContent((net.kyori.adventure.text.event.HoverEvent.ShowItem) wrappedObject.value());
        } else if (wrappedObject.action().type() == net.kyori.adventure.text.event.HoverEvent.ShowEntity.class) {
            return new AdventureEntityContent((net.kyori.adventure.text.event.HoverEvent.ShowEntity) wrappedObject.value());
        }
        return org.screamingsandals.lib.spectator.Component.empty(); // what?
    }

    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        try {
            return super.as(type);
        } catch (Throwable ignored) {
            return AdventureBackend.getAdditionalHoverEventConverter().convert(this, type);
        }
    }

    public static class AdventureHoverEventBuilder implements HoverEvent.Builder {
        private Action action = Action.SHOW_TEXT;
        private Content content;

        @Override
        @NotNull
        public Builder action(@NotNull Action action) {
            this.action = action;
            return this;
        }

        @Override
        @NotNull
        public Builder content(@NotNull Content content) {
            this.content = content;
            return this;
        }

        @Override
        @NotNull
        public HoverEvent build() {
            Preconditions.checkArgument(content != null, "Content is not specified!");
            switch (action) {
                case SHOW_TEXT:
                    Preconditions.checkArgument(content instanceof org.screamingsandals.lib.spectator.Component,
                            "Action type SHOW_TEXT requires a component to be present!");
                    return new AdventureHoverEvent(net.kyori.adventure.text.event.HoverEvent.showText(content.as(Component.class)));
                case SHOW_ENTITY:
                    Preconditions.checkArgument(content instanceof EntityContent, "Action type SHOW_ENTITY requires an EntityContent to be present!");
                    return new AdventureHoverEvent(net.kyori.adventure.text.event.HoverEvent.showEntity(content.as(net.kyori.adventure.text.event.HoverEvent.ShowEntity.class)));
                case SHOW_ITEM:
                    Preconditions.checkArgument(content instanceof ItemContent, "Action type SHOW_ITEM requires an ItemContent to be present!");
                    return new AdventureHoverEvent(net.kyori.adventure.text.event.HoverEvent.showItem(content.as(net.kyori.adventure.text.event.HoverEvent.ShowItem.class)));
                default:
                    throw new IllegalArgumentException("Action type must be specified!");
            }
        }
    }
}
