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

import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.EntityLiving;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.EntityProjectile;
import org.screamingsandals.lib.entity.ProjectileShooter;

public class BukkitEntityProjectile extends BukkitEntityBasic implements EntityProjectile {
    public BukkitEntityProjectile(Projectile wrappedObject) {
        super(wrappedObject);
    }

    @Override
    @Nullable
    public ProjectileShooter getShooter() {
        var source = ((Projectile) wrappedObject).getShooter();
        if (source instanceof Entity) {
            return EntityMapper.<EntityLiving>wrapEntity(source).orElseThrow();
        } else if (source instanceof BlockProjectileSource) {
            return new BukkitBlockProjectileSource((BlockProjectileSource) source);
        }
        return null;
    }

    @Override
    public void setShooter(@Nullable ProjectileShooter shooter) {
        ((Projectile) wrappedObject).setShooter(shooter != null ? shooter.as(ProjectileSource.class) : null);
    }

    @Override
    @Deprecated // deprecated for removal in the latest Paper
    public boolean doesBounce() {
        try {
            return ((Projectile) wrappedObject).doesBounce();
        } catch (Throwable ignored) {
            return false; // fix for future removal
        }
    }

    @Override
    @Deprecated // deprecated for removal in the latest Paper
    public void setBounce(boolean bounce) {
        try {
            ((Projectile) wrappedObject).setBounce(bounce);
        } catch (Throwable ignored) {
            // fix for future removal
        }
    }
}
