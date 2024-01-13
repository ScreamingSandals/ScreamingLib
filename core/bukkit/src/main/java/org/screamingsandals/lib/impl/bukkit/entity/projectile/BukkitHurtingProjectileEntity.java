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
import org.screamingsandals.lib.entity.projectile.HurtingProjectileEntity;

public class BukkitHurtingProjectileEntity extends BukkitProjectileEntity implements HurtingProjectileEntity {
    public BukkitHurtingProjectileEntity(@NotNull org.bukkit.entity.Fireball wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public float yield() {
        return ((org.bukkit.entity.Fireball) wrappedObject).getYield();
    }

    @Override
    public void yield(float yield) {
        ((org.bukkit.entity.Fireball) wrappedObject).setYield(yield);
    }

    @Override
    public boolean isIncendiary() {
        return ((org.bukkit.entity.Fireball) wrappedObject).isIncendiary();
    }

    @Override
    public void isIncendiary(boolean isIncendiary) {
        ((org.bukkit.entity.Fireball) wrappedObject).setIsIncendiary(isIncendiary);
    }
}
