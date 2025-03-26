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

package org.screamingsandals.lib.spectator.configurate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.event.ClickEvent;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Locale;

public class ClickEventSerializer implements TypeSerializer<ClickEvent> {
    public static final @NotNull ClickEventSerializer INSTANCE = new ClickEventSerializer();

    private static final @NotNull String ACTION_KEY = "action";
    private static final @NotNull String VALUE_KEY = "value";

    private static final @NotNull String URL_KEY = "url";
    private static final @NotNull String COMMAND_KEY = "command";
    private static final @NotNull String PAGE_KEY = "page";

    @Override
    public @NotNull ClickEvent deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
        try {
            var action = ClickEvent.Action.valueOf(node.node(ACTION_KEY).getString("open_url").toUpperCase(Locale.ROOT));
            String value;
            if (action == ClickEvent.Action.OPEN_URL && node.hasChild(URL_KEY)) {
                value = node.node(URL_KEY).getString("");
            } else if ((action == ClickEvent.Action.RUN_COMMAND || action == ClickEvent.Action.SUGGEST_COMMAND) && node.hasChild(COMMAND_KEY)) {
                value = node.node(COMMAND_KEY).getString("");
            } else if (action == ClickEvent.Action.CHANGE_PAGE && node.hasChild(PAGE_KEY)) {
                value = node.node(PAGE_KEY).getString("");
            } else {
                value = node.node(VALUE_KEY).getString("");
            }
            return ClickEvent.builder()
                    .action(action)
                    .value(value)
                    .build();
        } catch (Throwable throwable) {
            throw new SerializationException(throwable);
        }
    }

    @Override
    public void serialize(@NotNull Type type, @Nullable ClickEvent obj, @NotNull ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
            return;
        }

        node.node(ACTION_KEY).set(obj.action().name().toLowerCase(Locale.ROOT));
        switch (obj.action()) {
            case OPEN_URL:
                node.node(URL_KEY).set(obj.value());
                break;
            case RUN_COMMAND:
            case SUGGEST_COMMAND:
                node.node(COMMAND_KEY).set(obj.value());
                break;
            case CHANGE_PAGE:
                node.node(PAGE_KEY).set(obj.value());
                break;
            default:
                node.node(VALUE_KEY).set(obj.value());
        }
    }
}
