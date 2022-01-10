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

package org.screamingsandals.lib.bukkit.entity;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.util.Vector;
import org.screamingsandals.lib.entity.BlockProjectileShooter;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.EntityProjectile;
import org.screamingsandals.lib.entity.type.EntityTypeHolder;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;

import java.util.Optional;

public class BukkitBlockProjectileSource extends BasicWrapper<BlockProjectileSource> implements BlockProjectileShooter {
    public BukkitBlockProjectileSource(BlockProjectileSource wrappedObject) {
        super(wrappedObject);
    }

    @SuppressWarnings({"ConstantConditions", "unchecked"})
    @Override
    public Optional<EntityProjectile> launchProjectile(EntityTypeHolder projectileType) {
        return EntityMapper.wrapEntity(wrappedObject.launchProjectile((Class<Projectile>) projectileType.as(EntityType.class).getEntityClass()));
    }

    @SuppressWarnings({"ConstantConditions", "unchecked"})
    @Override
    public Optional<EntityProjectile> launchProjectile(EntityTypeHolder projectileType, Vector3D velocity) {
        return EntityMapper.wrapEntity(wrappedObject.launchProjectile((Class<Projectile>) projectileType.as(EntityType.class).getEntityClass(), new Vector(velocity.getX(), velocity.getY(), velocity.getZ())));
    }

    @Override
    public BlockHolder getBlock() {
        return BlockMapper.wrapBlock(wrappedObject.getBlock());
    }
}
