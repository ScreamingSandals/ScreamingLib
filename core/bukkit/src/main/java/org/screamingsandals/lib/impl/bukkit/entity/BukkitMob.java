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

package org.screamingsandals.lib.impl.bukkit.entity;

import org.bukkit.entity.Ambient;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Flying;
import org.bukkit.entity.Slime;
import org.bukkit.event.entity.EntityTargetEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.LivingEntity;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.entity.Mob;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;
import org.screamingsandals.lib.impl.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.nms.accessors.LivingEntityAccessor;
import org.screamingsandals.lib.nms.accessors.MobAccessor;
import org.screamingsandals.lib.utils.reflect.Reflect;

public class BukkitMob extends BukkitLivingEntity implements Mob {
    public BukkitMob(@NotNull org.bukkit.entity.LivingEntity wrappedObject) {
        super(wrappedObject);

        if (BukkitFeature.MOB_INTERFACE.isSupported()) {
            if (!(wrappedObject instanceof org.bukkit.entity.Mob)) {
                throw new IllegalArgumentException("Wrapped object is not an instance of Mob!");
            }
        } else if (!(wrappedObject instanceof Slime || wrappedObject instanceof Creature || wrappedObject instanceof Ambient || wrappedObject instanceof Flying || wrappedObject instanceof EnderDragon)) {
            throw new IllegalArgumentException("Wrapped object is not an instance of Slime, Creature, Ambient, Flying, WaterMob or EnderDragon!");
        }
    }

    @Override
    public void setCurrentTarget(@Nullable LivingEntity target) {
        var living = target == null ? null : target.as(org.bukkit.entity.LivingEntity.class);
        if (BukkitFeature.MOB_INTERFACE.isSupported() && (!(wrappedObject instanceof EnderDragon) || BukkitFeature.ENDER_DRAGON_EXTEND_MOB_INTERFACE_IN_API.isSupported())) {
            ((org.bukkit.entity.Mob) wrappedObject).setTarget(living);
        } else if (wrappedObject instanceof Slime) {
            if (BukkitFeature.SLIME_TARGET.isSupported()) {
                ((Slime) wrappedObject).setTarget(living);
            } else {
                Reflect.getMethod(ClassStorage.getHandle(wrappedObject), MobAccessor.getMethodSetTarget1().getName(), LivingEntityAccessor.getType(), EntityTargetEvent.TargetReason.class, boolean.class)
                        .invoke(target != null ? ClassStorage.getHandle(target) : null, null, false);
            }
        } else if (wrappedObject instanceof Creature) {
            ((Creature) wrappedObject).setTarget(living);
        } else {
            // TODO: what now?
        }
    }

    @Override
    public @Nullable LivingEntity getCurrentTarget() {
        org.bukkit.entity.LivingEntity living;
        if (BukkitFeature.MOB_INTERFACE.isSupported()) {
            living = ((org.bukkit.entity.Mob) wrappedObject).getTarget();
        } else if (wrappedObject instanceof Slime) {
            if (BukkitFeature.SLIME_TARGET.isSupported()) {
                living = ((Slime) wrappedObject).getTarget();
            } else { // <= 1.12.1
                var result = Reflect.fastInvoke(ClassStorage.getHandle(wrappedObject), MobAccessor.getMethodGetTarget1());
                if (result != null) {
                    return Entities.wrapEntityLiving(Reflect.fastInvoke(result, "getBukkitEntity"));
                }
                return null;
            }
        } else if (wrappedObject instanceof Creature) {
            living = ((Creature) wrappedObject).getTarget();
        } else {
            return null; // TODO: what now?
        }
        return Entities.wrapEntityLiving(living);
    }
}
