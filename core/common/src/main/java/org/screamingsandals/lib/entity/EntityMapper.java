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

package org.screamingsandals.lib.entity;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.entity.type.EntityTypeHolder;
import org.screamingsandals.lib.entity.type.EntityTypeMapping;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.meta.PotionEffectMapping;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;

import java.util.concurrent.CompletableFuture;

@AbstractService
@ServiceDependencies(dependsOn = {
        EntityTypeMapping.class,
        LocationMapper.class,
        PotionEffectMapping.class
})
public abstract class EntityMapper {

    private static @Nullable EntityMapper mapper;

    @ApiStatus.Internal
    public EntityMapper() {
        if (mapper != null) {
            throw new UnsupportedOperationException("EntityMapper is already initialized");
        }
        mapper = this;
    }

    @Contract("null -> null")
    public static @Nullable EntityBasic wrapEntity(@Nullable Object entity) {
        if (mapper == null) {
            throw new UnsupportedOperationException("EntityMapper is not initialized yet.");
        }
        if (entity == null) {
            return null;
        }
        if (entity instanceof EntityBasic) {
            return (EntityBasic) entity;
        }
        return mapper.wrapEntity0(entity);
    }

    @Contract("null -> null")
    public static @Nullable EntityExperience wrapEntityExperience(@Nullable Object entity) {
        var entityExperience = wrapEntity(entity);
        if (entityExperience instanceof EntityExperience) {
            return (EntityExperience) entityExperience;
        }
        return null;
    }

    @Contract("null -> null")
    public static @Nullable EntityFirework wrapEntityFirework(@Nullable Object entity) {
        var entityFirework = wrapEntity(entity);
        if (entityFirework instanceof EntityFirework) {
            return (EntityFirework) entityFirework;
        }
        return null;
    }

    @Contract("null -> null")
    public static @Nullable EntityHuman wrapEntityHuman(@Nullable Object entity) {
        var entityHuman = wrapEntity(entity);
        if (entityHuman instanceof EntityHuman) {
            return (EntityHuman) entityHuman;
        }
        return null;
    }

    @Contract("null -> null")
    public static @Nullable EntityItem wrapEntityItem(@Nullable Object entity) {
        var entityItem = wrapEntity(entity);
        if (entityItem instanceof EntityItem) {
            return (EntityItem) entityItem;
        }
        return null;
    }

    @Contract("null -> null")
    public static @Nullable EntityLightning wrapEntityLightning(@Nullable Object entity) {
        var entityLightning = wrapEntity(entity);
        if (entityLightning instanceof EntityLightning) {
            return (EntityLightning) entityLightning;
        }
        return null;
    }

    @Contract("null -> null")
    public static @Nullable EntityLiving wrapEntityLiving(@Nullable Object entity) {
        var entityLiving = wrapEntity(entity);
        if (entityLiving instanceof EntityLiving) {
            return (EntityLiving) entityLiving;
        }
        return null;
    }

    @Contract("null -> null")
    public static @Nullable EntityPathfindingMob wrapEntityPathfindingMob(@Nullable Object entity) {
        var entityPathfindingMob = wrapEntity(entity);
        if (entityPathfindingMob instanceof EntityPathfindingMob) {
            return (EntityPathfindingMob) entityPathfindingMob;
        }
        return null;
    }

    @Contract("null -> null")
    public static @Nullable EntityProjectile wrapEntityProjectile(@Nullable Object entity) {
        var entityProjectile = wrapEntity(entity);
        if (entityProjectile instanceof EntityProjectile) {
            return (EntityProjectile) entityProjectile;
        }
        return null;
    }

    @Contract("null,_ -> null")
    public static @Nullable EntityBasic spawn(@Nullable Object entityType, @NotNull LocationHolder locationHolder) {
        if (entityType == null) {
            return null;
        }
        if (entityType instanceof EntityTypeHolder) {
            return spawn((EntityTypeHolder) entityType, locationHolder);
        } else {
            var type = EntityTypeMapping.resolve(entityType);
            if (type != null) {
                return spawn(type, locationHolder);
            }
            return null;
        }
    }

    public static @Nullable EntityBasic spawn(@NotNull EntityTypeHolder entityType, @NotNull LocationHolder locationHolder) {
        if (mapper == null) {
            throw new UnsupportedOperationException("EntityMapper is not initialized yet.");
        }
        return mapper.spawn0(entityType, locationHolder);
    }

    public static @Nullable EntityItem dropItem(@NotNull Item item, @NotNull LocationHolder locationHolder) {
        if (mapper == null) {
            throw new UnsupportedOperationException("EntityMapper is not initialized yet.");
        }
        return mapper.dropItem0(item, locationHolder);
    }

    public static @Nullable EntityExperience dropExperience(int experience, @NotNull LocationHolder locationHolder) {
        if (mapper == null) {
            throw new UnsupportedOperationException("EntityMapper is not initialized yet.");
        }
        return mapper.dropExperience0(experience, locationHolder);
    }

    public static @Nullable EntityLightning strikeLightning(@NotNull LocationHolder locationHolder) {
        if (mapper == null) {
            throw new UnsupportedOperationException("EntityMapper is not initialized yet.");
        }
        return mapper.strikeLightning0(locationHolder);
    }

    public static int getNewEntityId() {
        if (mapper == null) {
            throw new UnsupportedOperationException("EntityMapper is not initialized yet.");
        }
        if (!Server.isServerThread()) {
            throw new UnsupportedOperationException("EntityMapper#getNewEntityId() method must be called from the Server thread!, or use EntityMapper#getNewEntityIdSynchronously() instead");
        }
        return mapper.getNewEntityId0();
    }

    public static @NotNull CompletableFuture<@NotNull Integer> getNewEntityIdSynchronously() {
        if (mapper == null) {
            throw new UnsupportedOperationException("EntityMapper is not initialized yet.");
        }
        return mapper.getNewEntityIdSynchronously0();
    }

    protected abstract @Nullable EntityBasic wrapEntity0(@NotNull Object entity);

    public abstract @Nullable EntityBasic spawn0(@NotNull EntityTypeHolder entityType, @NotNull LocationHolder locationHolder);

    public abstract @Nullable EntityItem dropItem0(@NotNull Item item, @NotNull LocationHolder locationHolder);

    public abstract @Nullable EntityExperience dropExperience0(int experience, @NotNull LocationHolder locationHolder);

    public abstract @Nullable EntityLightning strikeLightning0(@NotNull LocationHolder locationHolder);

    public abstract int getNewEntityId0();

    public abstract @NotNull CompletableFuture<@NotNull Integer> getNewEntityIdSynchronously0();
}
