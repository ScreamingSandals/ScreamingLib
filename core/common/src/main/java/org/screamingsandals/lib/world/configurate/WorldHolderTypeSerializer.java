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

package org.screamingsandals.lib.world.configurate;

import lombok.experimental.ExtensionMethod;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.extensions.NullableExtension;
import org.screamingsandals.lib.world.WorldHolder;
import org.screamingsandals.lib.world.WorldMapper;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.UUID;

@ExtensionMethod(value = {NullableExtension.class}, suppressBaseMethods = false)
public class WorldHolderTypeSerializer implements TypeSerializer<WorldHolder> {
    private final String UUID_FIELD = "uuid";

    @Override
    public WorldHolder deserialize(Type type, ConfigurationNode node) throws SerializationException {
        return WorldMapper.getWorld(node.node(UUID_FIELD).get(UUID.class)).orElseThrow();
    }

    @Override
    public void serialize(Type type, @Nullable WorldHolder holder, ConfigurationNode node) throws SerializationException {
        if (holder == null) {
            node.raw(null);
            return;
        }
        node.node(UUID_FIELD).set(holder.getUuid());
    }
}
