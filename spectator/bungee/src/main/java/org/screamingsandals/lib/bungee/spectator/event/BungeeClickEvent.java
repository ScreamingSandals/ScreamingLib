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

package org.screamingsandals.lib.bungee.spectator.event;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.bungee.spectator.AbstractBungeeBackend;
import org.screamingsandals.lib.spectator.event.ClickEvent;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.Preconditions;

public class BungeeClickEvent extends BasicWrapper<net.md_5.bungee.api.chat.ClickEvent> implements ClickEvent {
    public BungeeClickEvent(net.md_5.bungee.api.chat.ClickEvent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    @NotNull
    public Action action() {
        try {
            return Action.valueOf(wrappedObject.getAction().name());
        } catch (Throwable ignored) {
            return Action.OPEN_URL; // ig COPY_TO_CLIPBOARD have been used
        }
    }

    @Override
    @NotNull
    public ClickEvent withAction(@NotNull Action action) {
        net.md_5.bungee.api.chat.ClickEvent.Action bungeeAction;
        try {
            bungeeAction = net.md_5.bungee.api.chat.ClickEvent.Action.valueOf(action.name());
        } catch (Throwable throwable) {
            bungeeAction = net.md_5.bungee.api.chat.ClickEvent.Action.OPEN_URL;
        }
        return new BungeeClickEvent(new net.md_5.bungee.api.chat.ClickEvent(bungeeAction, wrappedObject.getValue()));
    }

    @Override
    @NotNull
    public String value() {
        return wrappedObject.getValue();
    }

    @Override
    @NotNull
    public ClickEvent withValue(@NotNull String value) {
        return new BungeeClickEvent(new net.md_5.bungee.api.chat.ClickEvent(wrappedObject.getAction(), value));
    }

    @Override
    @NotNull
    public ClickEvent.Builder toBuilder() {
        return new BungeeClickBuilder(action(), value());
    }

    @Override
    public <T> T as(Class<T> type) {
        try {
            return super.as(type);
        } catch (Throwable ignored) {
            return AbstractBungeeBackend.getAdditionalClickEventConverter().convert(this, type);
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(fluent = true, chain = true)
    @Setter
    public static class BungeeClickBuilder implements ClickEvent.Builder {
        private Action action = Action.OPEN_URL;
        private String value;

        @Override
        @NotNull
        public ClickEvent build() {
            Preconditions.checkNotNull(action, "Action is not specified!");
            Preconditions.checkNotNull(value, "Value is not specified!");
            net.md_5.bungee.api.chat.ClickEvent.Action action;
            try {
                action = net.md_5.bungee.api.chat.ClickEvent.Action.valueOf(this.action.name());
            } catch (Throwable throwable) {
                action = net.md_5.bungee.api.chat.ClickEvent.Action.OPEN_URL;
            }
            return new BungeeClickEvent(new net.md_5.bungee.api.chat.ClickEvent(action, value));
        }
    }
}