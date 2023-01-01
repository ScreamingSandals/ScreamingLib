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

package org.screamingsandals.lib.spectator.configurate;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.event.ClickEvent;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Locale;

public class ClickEventSerializer implements TypeSerializer<ClickEvent> {
    public static final ClickEventSerializer INSTANCE = new ClickEventSerializer();

    private static final String ACTION_KEY = "action";
    private static final String VALUE_KEY = "value";

    @Override
    public ClickEvent deserialize(Type type, ConfigurationNode node) throws SerializationException {
        try {
            var action = ClickEvent.Action.valueOf(node.node(ACTION_KEY).getString("open_url").toUpperCase(Locale.ROOT));
            var value = node.node(VALUE_KEY).getString("");
            return ClickEvent.builder()
                    .action(action)
                    .value(value)
                    .build();
        } catch (Throwable throwable) {
            throw new SerializationException(throwable);
        }
    }

    @Override
    public void serialize(Type type, @Nullable ClickEvent obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
            return;
        }

        node.node(ACTION_KEY).set(obj.action().name().toLowerCase(Locale.ROOT));
        node.node(VALUE_KEY).set(obj.value());
    }
}
