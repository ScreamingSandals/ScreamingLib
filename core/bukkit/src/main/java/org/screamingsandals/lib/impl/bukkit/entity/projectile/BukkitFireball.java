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

package org.screamingsandals.lib.impl.bukkit.entity.projectile;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.entity.projectile.Fireball;
import org.screamingsandals.lib.entity.projectile.HurtingProjectileEntity;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;

public class BukkitFireball extends BukkitHurtingProjectileEntity implements Fireball {
    public BukkitFireball(@NotNull org.bukkit.entity.Fireball wrappedObject) {
        super(wrappedObject);
        if (BukkitFeature.ENTITY_SIZED_FIREBALL.isSupported()) {
            if (!(wrappedObject instanceof org.bukkit.entity.SizedFireball)) {
                throw new IllegalArgumentException("Wrapped object is not instance of Fireball!");
            }
        } else {
            if (!(wrappedObject instanceof org.bukkit.entity.SmallFireball) && !(wrappedObject instanceof org.bukkit.entity.LargeFireball)) {
                throw new IllegalArgumentException("Wrapped object is not instance of Fireball!");
            }
        }
    }
}