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

import org.jetbrains.annotations.*;
import org.screamingsandals.lib.impl.entity.pose.EntityPoseRegistry;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.annotations.ide.MinecraftType;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;
import org.screamingsandals.lib.impl.utils.registry.RegistryItem;
import org.screamingsandals.lib.impl.utils.registry.RegistryItemStream;

@LimitedVersionSupport("Bukkit >= 1.17")
public interface EntityPose extends RegistryItem, RawValueHolder {
    @ApiStatus.Experimental
    @NotNull String platformName();

    /**
     * {@inheritDoc}
     */
    @Override
    boolean is(@MinecraftType(MinecraftType.Type.ENTITY_POSE) @Nullable Object object);

    @Override
    boolean is(@MinecraftType(MinecraftType.Type.ENTITY_POSE) @Nullable Object @NotNull... objects);

    static @NotNull EntityPose of(@MinecraftType(MinecraftType.Type.ENTITY_POSE) @NotNull Object entityPose) {
        var result = ofNullable(entityPose);
        Preconditions.checkNotNullIllegal(result, "Could not find entity pose: " + entityPose);
        return result;
    }

    @Contract("null -> null")
    static @Nullable EntityPose ofNullable(@MinecraftType(MinecraftType.Type.ENTITY_POSE) @Nullable Object entityPose) {
        if (entityPose instanceof EntityPose) {
            return (EntityPose) entityPose;
        }
        return EntityPoseRegistry.getInstance().resolveMapping(entityPose);
    }

    static @NotNull RegistryItemStream<@NotNull EntityPose> all() {
        return EntityPoseRegistry.getInstance().getRegistryItemStream();
    }
}
