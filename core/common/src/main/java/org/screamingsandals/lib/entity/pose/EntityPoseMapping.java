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

package org.screamingsandals.lib.entity.pose;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.configurate.EntityPoseHolderSerializer;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.annotations.ide.OfMethodAlternative;
import org.screamingsandals.lib.utils.mapper.AbstractTypeMapper;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Collections;
import java.util.List;

@AbstractService(
        pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+\\.[^\\.]+)\\.(?<className>.+)$"
)
public abstract class EntityPoseMapping extends AbstractTypeMapper<EntityPoseHolder> {
    private static @Nullable EntityPoseMapping entityPoseMapping;

    protected final @NotNull BidirectionalConverter<EntityPoseHolder> entityPoseConverter = BidirectionalConverter.<EntityPoseHolder>build()
            .registerP2W(EntityPoseHolder.class, d -> d)
            .registerP2W(ConfigurationNode.class, node -> {
                try {
                    return EntityPoseHolderSerializer.INSTANCE.deserialize(EntityPoseHolder.class, node);
                } catch (SerializationException ex) {
                    ex.printStackTrace();
                    return null;
                }
            });

    @ApiStatus.Internal
    public EntityPoseMapping() {
        if (entityPoseMapping != null) {
            throw new UnsupportedOperationException("EntityPoseMapping is already initialized!");
        }
        entityPoseMapping = this;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.ENTITY_POSE)
    @OfMethodAlternative(value = EntityPoseHolder.class, methodName = "ofNullable")
    @Contract("null -> null")
    public static @Nullable EntityPoseHolder resolve(@Nullable Object entityPose) {
        if (entityPoseMapping == null) {
            throw new UnsupportedOperationException("EntityPoseMapping is not initialized yet.");
        }

        if (entityPose == null) {
            return null;
        }

        return entityPoseMapping.entityPoseConverter.convertOptional(entityPose).or(() -> entityPoseMapping.resolveFromMapping(entityPose)).orElse(null);
    }

    @OfMethodAlternative(value = EntityPoseHolder.class, methodName = "all")
    public static @NotNull List<@NotNull EntityPoseHolder> getValues() {
        if (entityPoseMapping == null) {
            throw new UnsupportedOperationException("EntityPoseMapping is not initialized yet.");
        }
        return Collections.unmodifiableList(entityPoseMapping.values);
    }
}
