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

import org.screamingsandals.lib.spectator.event.ClickEvent;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.Preconditions;

public class AdventureClickEvent extends BasicWrapper<net.kyori.adventure.text.event.ClickEvent> implements ClickEvent {
    public AdventureClickEvent(net.kyori.adventure.text.event.ClickEvent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public Action action() {
        try {
            return Action.valueOf(wrappedObject.action().name());
        } catch (Throwable ignored) {
            return Action.OPEN_URL; // HOW??
        }
    }

    @Override
    public String value() {
        return wrappedObject.value();
    }

    public static class AdventureClickEventBuilder implements ClickEvent.Builder {
        private Action action = Action.OPEN_URL;
        private String value;

        @Override
        public Builder action(Action action) {
            this.action = action;
            return this;
        }

        @Override
        public Builder value(String value) {
            this.value = value;
            return this;
        }

        @Override
        public ClickEvent build() {
            Preconditions.checkArgument(value != null, "Value is not specified!");
            return new AdventureClickEvent(net.kyori.adventure.text.event.ClickEvent.clickEvent(net.kyori.adventure.text.event.ClickEvent.Action.valueOf(action.name()), value));
        }
    }
}
