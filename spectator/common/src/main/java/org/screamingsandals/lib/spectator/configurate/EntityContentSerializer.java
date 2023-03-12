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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.event.hover.EntityContent;
import org.screamingsandals.lib.utils.key.ResourceLocation;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.UUID;

public class EntityContentSerializer implements TypeSerializer<EntityContent> {
    public static final @NotNull EntityContentSerializer INSTANCE = new EntityContentSerializer();

    private static final @NotNull String TYPE_KEY = "type";
    private static final @NotNull String ID_KEY = "id";
    private static final @NotNull String NAME_KEY = "name";

    @Override
    public @NotNull EntityContent deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
        try {
            var entityType = ResourceLocation.of(node.node(TYPE_KEY).getString("minecraft:pig"));
            var id = node.node(ID_KEY).get(UUID.class, UUID.randomUUID());
            @Nullable var name = node.node(NAME_KEY).get(Component.class);

            return EntityContent.builder()
                    .type(entityType)
                    .id(id)
                    .name(name)
                    .build();
        } catch (Throwable throwable) {
            throw new SerializationException(throwable);
        }
    }

    @Override
    public void serialize(@NotNull Type type, @Nullable EntityContent obj, @NotNull ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
            return;
        }

        node.node(TYPE_KEY).set(obj.type().asString());
        node.node(ID_KEY).set(UUID.class, obj.id());
        node.node(NAME_KEY).set(Component.class, obj.name());
    }
}
