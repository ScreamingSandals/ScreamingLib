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

package org.screamingsandals.lib.impl.bukkit.entity.projectile;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.entity.projectile.ThrowableProjectileEntity;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;
import org.screamingsandals.lib.utils.Preconditions;

public class BukkitThrowableProjectileEntity extends BukkitProjectileEntity implements ThrowableProjectileEntity {
    public BukkitThrowableProjectileEntity(@NotNull org.bukkit.entity.Projectile wrappedObject) {
        super(wrappedObject);
        if (BukkitFeature.ENTITY_THROWABLE_PROJECTILE.isSupported()) {
            if (!BukkitFeature.ENTITY_THROWN_POTION_EXTENDS_THROWABLE_PROJECTILE.isSupported()) {
                if (wrappedObject instanceof org.bukkit.entity.ThrownPotion) {
                    return; // ok
                }
            }
            Preconditions.checkArgument(wrappedObject instanceof org.bukkit.entity.ThrowableProjectile, "Must be an instance of ThrowableProjectile");
        } else {
            Preconditions.checkArgument(
                    wrappedObject instanceof org.bukkit.entity.ThrownExpBottle ||
                            wrappedObject instanceof org.bukkit.entity.Egg ||
                            wrappedObject instanceof org.bukkit.entity.EnderPearl ||
                            wrappedObject instanceof org.bukkit.entity.Snowball ||
                            wrappedObject instanceof org.bukkit.entity.ThrownPotion,
                    "Must be an instance of ThrownExpBottle, Egg, EnderPearl, Snowball or ThrownPotion");
        }
    }
}
