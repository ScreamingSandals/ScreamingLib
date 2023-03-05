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

package org.screamingsandals.lib.bukkit.entity;

import lombok.experimental.ExtensionMethod;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.entity.*;
import org.screamingsandals.lib.entity.ExperienceOrb;
import org.screamingsandals.lib.entity.LightningStrike;
import org.screamingsandals.lib.entity.type.EntityTypeHolder;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.nms.accessors.*;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.extensions.NullableExtension;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.Location;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@ExtensionMethod(value = NullableExtension.class, suppressBaseMethods = false)
@Service
public class BukkitEntities extends Entities {

    public static final boolean HAS_MOB_INTERFACE = Reflect.has("org.bukkit.entity.Mob");

    @Override
    protected @Nullable BasicEntity wrapEntity0(@NotNull Object entity) {
        if (entity instanceof BasicEntity) {
            return (BasicEntity) entity;
        }

        if (!(entity instanceof Entity)) {
            return null;
        }

        // order is important here
        if (entity instanceof Player) {
            return new BukkitPlayer((Player) entity);
        }

        if (entity instanceof org.bukkit.entity.HumanEntity) {
            return new BukkitHumanEntity((org.bukkit.entity.HumanEntity) entity);
        }

        if (HAS_MOB_INTERFACE) {
            if (entity instanceof Mob) {
                return new BukkitPathfindingMob((org.bukkit.entity.LivingEntity) entity);
            }
        } else if (entity instanceof Slime || entity instanceof Creature) {
            return new BukkitPathfindingMob((org.bukkit.entity.LivingEntity) entity);
        }

        if (entity instanceof org.bukkit.entity.LivingEntity) {
            return new BukkitLivingEntity((org.bukkit.entity.LivingEntity) entity);
        }

        if (entity instanceof org.bukkit.entity.Firework) {
            return new BukkitFirework((org.bukkit.entity.Firework) entity);
        }

        if (entity instanceof Projectile) {
            return new BukkitProjectileEntity((Projectile) entity);
        }

        if (entity instanceof Item) {
            return new BukkitItemEntity((Item) entity);
        }

        if (entity instanceof org.bukkit.entity.LightningStrike) {
            return new BukkitLightningStrike((org.bukkit.entity.LightningStrike) entity);
        }

        if (entity instanceof org.bukkit.entity.ExperienceOrb) {
            return new BukkitExperienceOrb((org.bukkit.entity.ExperienceOrb) entity);
        }

        return new BukkitBasicEntity((Entity) entity);
    }

    @Override
    public @Nullable BasicEntity spawn0(@NotNull EntityTypeHolder entityType, @NotNull Location locationHolder) {
        return entityType.asNullable(EntityType.class).mapOrNull(entityType1 -> {
            var bukkitLoc = locationHolder.as(org.bukkit.Location.class);
            var world = bukkitLoc.getWorld();
            if (world != null) {
                // TODO: test all entity types
                var entity = world.spawnEntity(bukkitLoc, entityType1);
                return wrapEntity0(entity);
            }
            return null;
        });
    }

    @Override
    public @Nullable ItemEntity dropItem0(@NotNull ItemStack item, @NotNull Location locationHolder) {
        var bukkitLoc = locationHolder.as(org.bukkit.Location.class);
        var itemEntity = bukkitLoc.getWorld().dropItem(bukkitLoc, item.as(org.bukkit.inventory.ItemStack.class));
        return new BukkitItemEntity(itemEntity);
    }

    @Override
    public @Nullable ExperienceOrb dropExperience0(int experience, @NotNull Location locationHolder) {
        var bukkitLoc = locationHolder.as(org.bukkit.Location.class);
        var orb = (org.bukkit.entity.ExperienceOrb) bukkitLoc.getWorld().spawnEntity(bukkitLoc, EntityType.EXPERIENCE_ORB);
        orb.setExperience(experience);
        return new BukkitExperienceOrb(orb);
    }

    @Override
    public @Nullable LightningStrike strikeLightning0(@NotNull Location locationHolder) {
        var bukkitLoc = locationHolder.as(org.bukkit.Location.class);
        var lightning = bukkitLoc.getWorld().strikeLightning(bukkitLoc);
        return new BukkitLightningStrike(lightning);
    }

    @Override
    public int getNewEntityId0() {
        final var entityCount = Reflect.getField(EntityAccessor.getFieldField_70152_a());
        if (entityCount != null) {
            if (entityCount instanceof AtomicInteger) {
                return ((AtomicInteger) entityCount).incrementAndGet();
            }
            final var newCount = ((int) entityCount) + 1;
            Reflect.setField(EntityAccessor.getFieldField_70152_a(), newCount);
            return (int) entityCount;
        }

        final var entityCounter = Reflect.getField(EntityAccessor.getFieldENTITY_COUNTER());
        if (entityCounter instanceof AtomicInteger) {
            return ((AtomicInteger) entityCounter).incrementAndGet();
        }
        throw new UnsupportedOperationException("Can't obtain new Entity id");
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Integer> getNewEntityIdSynchronously0() {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        if (Server.isServerThread()) {
            future.complete(getNewEntityId());
        } else {
            Server.runSynchronously(() -> future.complete(getNewEntityId()));
        }
        future.exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });
        return future;
    }
}
