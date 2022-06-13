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

package org.screamingsandals.lib.spectator.configurate;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.event.hover.ItemContent;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class ItemContentSerializer implements TypeSerializer<ItemContent> {
    public static final ItemContentSerializer INSTANCE = new ItemContentSerializer();

    private static final String ID_KEY = "id";
    private static final String COUNT_KEY = "count";
    private static final String TAG_KEY = "tag";

    @Override
    public ItemContent deserialize(Type type, ConfigurationNode node) throws SerializationException {
        try {
            var id = NamespacedMappingKey.of(node.node(ID_KEY).getString("minecraft:air"));
            var count = node.node(COUNT_KEY).getInt(1);
            @Nullable
            var tag = node.node(TAG_KEY).getString();
            return ItemContent.builder()
                    .id(id)
                    .count(count)
                    .tag(tag)
                    .build();
        } catch (Throwable throwable) {
            throw new SerializationException(throwable);
        }
    }

    @Override
    public void serialize(Type type, @Nullable ItemContent obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
            return;
        }

        node.node(ID_KEY).set(obj.id().asString());
        node.node(COUNT_KEY).set(obj.count());
        node.node(TAG_KEY).set(obj.tag());
    }
}
