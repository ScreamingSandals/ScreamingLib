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

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.block.BukkitBlock;
import org.screamingsandals.lib.entity.BlockProjectileShooter;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.entity.ProjectileEntity;
import org.screamingsandals.lib.entity.type.EntityTypeHolder;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.block.Block;

public class BukkitBlockProjectileSource extends BasicWrapper<BlockProjectileSource> implements BlockProjectileShooter {
    public BukkitBlockProjectileSource(@NotNull BlockProjectileSource wrappedObject) {
        super(wrappedObject);
    }

    @SuppressWarnings({"ConstantConditions", "unchecked"})
    @Override
    public @Nullable ProjectileEntity launchProjectile(@NotNull EntityTypeHolder projectileType) {
        var projectileBukkit = projectileType.as(EntityType.class).getEntityClass();
        if (!Projectile.class.isAssignableFrom(projectileBukkit)) {
            return null;
        }
        return Entities.wrapEntityProjectile(wrappedObject.launchProjectile((Class<Projectile>) projectileBukkit));
    }

    @SuppressWarnings({"ConstantConditions", "unchecked"})
    @Override
    public @Nullable ProjectileEntity launchProjectile(@NotNull EntityTypeHolder projectileType, @NotNull Vector3D velocity) {
        var projectileBukkit = projectileType.as(EntityType.class).getEntityClass();
        if (!Projectile.class.isAssignableFrom(projectileBukkit)) {
            return null;
        }
        return Entities.wrapEntityProjectile(wrappedObject.launchProjectile((Class<Projectile>) projectileBukkit, new Vector(velocity.getX(), velocity.getY(), velocity.getZ())));
    }

    @Override
    public @NotNull Block getBlock() {
        return new BukkitBlock(wrappedObject.getBlock());
    }
}
