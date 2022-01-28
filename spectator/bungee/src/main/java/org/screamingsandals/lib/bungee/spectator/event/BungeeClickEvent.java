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

import org.jetbrains.annotations.NotNull;
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
    public String value() {
        return wrappedObject.getValue();
    }

    public static class BungeeClickBuilder implements ClickEvent.Builder {
        private Action action = Action.OPEN_URL;
        private String value;

        @Override
        @NotNull
        public Builder action(@NotNull Action action) {
            this.action = action;
            return this;
        }

        @Override
        @NotNull
        public Builder value(@NotNull String value) {
            this.value = value;
            return this;
        }

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
