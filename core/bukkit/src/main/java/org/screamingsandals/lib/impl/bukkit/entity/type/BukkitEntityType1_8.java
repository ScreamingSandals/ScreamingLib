/*
 * Copyright 2024 ScreamingSandals
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

package org.screamingsandals.lib.impl.bukkit.entity.type;

import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.Entity;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.entity.type.EntityType;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.world.Location;

import java.util.Arrays;

public class BukkitEntityType1_8 extends BasicWrapper<org.bukkit.entity.EntityType> implements EntityType {
    @Getter
    private final byte additionalLegacyData;

    public BukkitEntityType1_8(@NotNull org.bukkit.entity.EntityType wrappedObject) {
        super(wrappedObject);
        this.additionalLegacyData = 0;
    }

    @ApiStatus.Experimental // should not be used in modern environments
    public BukkitEntityType1_8(@NotNull org.bukkit.entity.EntityType wrappedObject, byte additionalLegacyData) {
        super(wrappedObject);
        this.additionalLegacyData = additionalLegacyData;
    }

    @Override
    public @NotNull String platformName() {
        return wrappedObject.name();
    }

    @Override
    public boolean isAlive() {
        return wrappedObject.isAlive();
    }

    @Override
    public boolean hasTag(@NotNull Object tag) {
        ResourceLocation key;
        if (tag instanceof ResourceLocation) {
            key = (ResourceLocation) tag;
        } else {
            key = ResourceLocation.of(tag.toString());
        }
        // backported tags
        if (!"minecraft".equals(key.namespace())) {
            return false;
        }
        var value = key.path();
        return BukkitEntityTypeRegistry.hasTagInBackPorts(wrappedObject, value);
    }

    @Override
    public boolean is(@Nullable Object entityType) {
        if (entityType instanceof org.bukkit.entity.EntityType || entityType instanceof EntityType) {
            return equals(entityType);
        }
        if (entityType instanceof String) {
            var str = (String) entityType;
            if (str.startsWith("#")) {
                // seems like a tag
                return hasTag(str.substring(1));
            }
        }
        return equals(EntityType.ofNullable(entityType));
    }

    @Override
    public boolean is(@Nullable Object @NotNull... entityTypes) {
        return Arrays.stream(entityTypes).anyMatch(this::is);
    }

    @Override
    public @NotNull ResourceLocation location() {
        return InternalEntityLegacyConstants.translateLegacyName1_8(wrappedObject, additionalLegacyData);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj) && (!(obj instanceof BukkitEntityType1_8) || ((BukkitEntityType1_8) obj).additionalLegacyData == this.additionalLegacyData);
    }
}
