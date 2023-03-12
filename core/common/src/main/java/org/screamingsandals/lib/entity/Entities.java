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
import org.screamingsandals.lib.entity.type.EntityType;
import org.screamingsandals.lib.entity.type.EntityTypeRegistry;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.item.meta.PotionEffectMapping;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;
import org.screamingsandals.lib.world.Location;
import org.screamingsandals.lib.world.Locations;

import java.util.concurrent.CompletableFuture;

@AbstractService
@ServiceDependencies(dependsOn = {
        EntityTypeRegistry.class,
        Locations.class,
        PotionEffectMapping.class
})
public abstract class Entities {

    private static @Nullable Entities mapper;

    @ApiStatus.Internal
    public Entities() {
        if (mapper != null) {
            throw new UnsupportedOperationException("EntityMapper is already initialized");
        }
        mapper = this;
    }

    @Contract("null -> null")
    public static @Nullable BasicEntity wrapEntity(@Nullable Object entity) {
        if (mapper == null) {
            throw new UnsupportedOperationException("EntityMapper is not initialized yet.");
        }
        if (entity == null) {
            return null;
        }
        if (entity instanceof BasicEntity) {
            return (BasicEntity) entity;
        }
        return mapper.wrapEntity0(entity);
    }

    @Contract("null -> null")
    public static @Nullable ExperienceOrb wrapEntityExperience(@Nullable Object entity) {
        var entityExperience = wrapEntity(entity);
        if (entityExperience instanceof ExperienceOrb) {
            return (ExperienceOrb) entityExperience;
        }
        return null;
    }

    @Contract("null -> null")
    public static @Nullable Firework wrapEntityFirework(@Nullable Object entity) {
        var entityFirework = wrapEntity(entity);
        if (entityFirework instanceof Firework) {
            return (Firework) entityFirework;
        }
        return null;
    }

    @Contract("null -> null")
    public static @Nullable HumanEntity wrapEntityHuman(@Nullable Object entity) {
        var entityHuman = wrapEntity(entity);
        if (entityHuman instanceof HumanEntity) {
            return (HumanEntity) entityHuman;
        }
        return null;
    }

    @Contract("null -> null")
    public static @Nullable ItemEntity wrapEntityItem(@Nullable Object entity) {
        var entityItem = wrapEntity(entity);
        if (entityItem instanceof ItemEntity) {
            return (ItemEntity) entityItem;
        }
        return null;
    }

    @Contract("null -> null")
    public static @Nullable LightningStrike wrapEntityLightning(@Nullable Object entity) {
        var entityLightning = wrapEntity(entity);
        if (entityLightning instanceof LightningStrike) {
            return (LightningStrike) entityLightning;
        }
        return null;
    }

    @Contract("null -> null")
    public static @Nullable LivingEntity wrapEntityLiving(@Nullable Object entity) {
        var entityLiving = wrapEntity(entity);
        if (entityLiving instanceof LivingEntity) {
            return (LivingEntity) entityLiving;
        }
        return null;
    }

    @Contract("null -> null")
    public static @Nullable PathfindingMob wrapEntityPathfindingMob(@Nullable Object entity) {
        var entityPathfindingMob = wrapEntity(entity);
        if (entityPathfindingMob instanceof PathfindingMob) {
            return (PathfindingMob) entityPathfindingMob;
        }
        return null;
    }

    @Contract("null -> null")
    public static @Nullable ProjectileEntity wrapEntityProjectile(@Nullable Object entity) {
        var entityProjectile = wrapEntity(entity);
        if (entityProjectile instanceof ProjectileEntity) {
            return (ProjectileEntity) entityProjectile;
        }
        return null;
    }

    @Contract("null,_ -> null")
    public static @Nullable BasicEntity spawn(@Nullable Object entityType, @NotNull Location locationHolder) {
        if (entityType == null) {
            return null;
        }
        if (entityType instanceof EntityType) {
            return spawn((EntityType) entityType, locationHolder);
        } else {
            var type = EntityTypeRegistry.resolve(entityType);
            if (type != null) {
                return spawn(type, locationHolder);
            }
            return null;
        }
    }

    public static @Nullable BasicEntity spawn(@NotNull EntityType entityType, @NotNull Location locationHolder) {
        if (mapper == null) {
            throw new UnsupportedOperationException("EntityMapper is not initialized yet.");
        }
        return mapper.spawn0(entityType, locationHolder);
    }

    public static @Nullable ItemEntity dropItem(@NotNull ItemStack item, @NotNull Location locationHolder) {
        if (mapper == null) {
            throw new UnsupportedOperationException("EntityMapper is not initialized yet.");
        }
        return mapper.dropItem0(item, locationHolder);
    }

    public static @Nullable ExperienceOrb dropExperience(int experience, @NotNull Location locationHolder) {
        if (mapper == null) {
            throw new UnsupportedOperationException("EntityMapper is not initialized yet.");
        }
        return mapper.dropExperience0(experience, locationHolder);
    }

    public static @Nullable LightningStrike strikeLightning(@NotNull Location locationHolder) {
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

    protected abstract @Nullable BasicEntity wrapEntity0(@NotNull Object entity);

    public abstract @Nullable BasicEntity spawn0(@NotNull EntityType entityType, @NotNull Location locationHolder);

    public abstract @Nullable ItemEntity dropItem0(@NotNull ItemStack item, @NotNull Location locationHolder);

    public abstract @Nullable ExperienceOrb dropExperience0(int experience, @NotNull Location locationHolder);

    public abstract @Nullable LightningStrike strikeLightning0(@NotNull Location locationHolder);

    public abstract int getNewEntityId0();

    public abstract @NotNull CompletableFuture<@NotNull Integer> getNewEntityIdSynchronously0();
}
