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

import org.bukkit.entity.Creature;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Slime;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.LivingEntity;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.entity.PathfindingMob;

public class BukkitPathfindingMob extends BukkitLivingEntity implements PathfindingMob {
    public BukkitPathfindingMob(@NotNull org.bukkit.entity.LivingEntity wrappedObject) {
        super(wrappedObject);

        if (BukkitEntities.HAS_MOB_INTERFACE) {
            if (!(wrappedObject instanceof Mob)) {
                throw new UnsupportedOperationException("Wrapped object is not instance of Mob!");
            }
        } else if (!(wrappedObject instanceof Slime || wrappedObject instanceof Creature)) {
            throw new UnsupportedOperationException("Wrapped object is not instance of Slime or Creature!");
        }
    }

    @Override
    public void setCurrentTarget(@Nullable LivingEntity target) {
        var living = target == null ? null : target.as(org.bukkit.entity.LivingEntity.class);
        if (BukkitEntities.HAS_MOB_INTERFACE) {
            ((Mob) wrappedObject).setTarget(living);
        } else if (wrappedObject instanceof Slime) {
            ((Slime) wrappedObject).setTarget(living);
        } else {
            ((Creature) wrappedObject).setTarget(living);
        }
    }

    @Override
    public @Nullable LivingEntity getCurrentTarget() {
        org.bukkit.entity.LivingEntity living;
        if (BukkitEntities.HAS_MOB_INTERFACE) {
            living = ((Mob) wrappedObject).getTarget();
        } else if (wrappedObject instanceof Slime) {
            living = ((Slime) wrappedObject).getTarget();
        } else {
            living = ((Creature) wrappedObject).getTarget();
        }
        return Entities.wrapEntityLiving(living);
    }
}
