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

package org.screamingsandals.lib.configurate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.pose.EntityPoseHolder;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class EntityPoseHolderSerializer extends AbstractScreamingSerializer implements TypeSerializer<EntityPoseHolder> {
    public static final @NotNull EntityPoseHolderSerializer INSTANCE = new EntityPoseHolderSerializer();

    @Override
    public @NotNull EntityPoseHolder deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
        try {
            return EntityPoseHolder.of(node.getString());
        } catch (Throwable t) {
            throw new SerializationException(t);
        }
    }

    @Override
    public void serialize(@NotNull Type type, @Nullable EntityPoseHolder obj, @NotNull ConfigurationNode node) throws SerializationException {
        node.set(obj == null ? null : obj.platformName());
    }
}
