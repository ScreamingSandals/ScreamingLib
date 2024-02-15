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

package org.screamingsandals.lib.impl.bukkit.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.entity.Entity;
import org.screamingsandals.lib.entity.PrimedTnt;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;
import org.screamingsandals.lib.impl.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.impl.nms.accessors.PrimedTntAccessor;
import org.screamingsandals.lib.utils.reflect.Reflect;

public class BukkitPrimedTnt extends BukkitEntity implements PrimedTnt {
    public BukkitPrimedTnt(@NotNull org.bukkit.entity.TNTPrimed wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public int fuseTicks() {
        return ((org.bukkit.entity.TNTPrimed) wrappedObject).getFuseTicks();
    }

    @Override
    public void fuseTicks(int fuseTicks) {
        ((org.bukkit.entity.TNTPrimed) wrappedObject).setFuseTicks(fuseTicks);
    }

    @Override
    public @Nullable Entity source() {
        var entity = ((org.bukkit.entity.TNTPrimed) wrappedObject).getSource();
        if (entity != null) {
            return Entities.wrapEntity(entity);
        }
        return null;
    }

    @Override
    public void source(@Nullable Entity source) {
        if (BukkitFeature.ENTITY_PRIMED_TNT_SET_SOURCE.isSupported()) {
            ((org.bukkit.entity.TNTPrimed) wrappedObject).setSource(source != null ? source.as(org.bukkit.entity.Entity.class) : null);
        } else {
            Reflect.setField(ClassStorage.getHandle(wrappedObject), PrimedTntAccessor.FIELD_OWNER.get(), source != null ? ClassStorage.getHandle(source.as(org.bukkit.entity.Entity.class)) : null);
        }
    }

    @Override
    public float yield() {
        return ((org.bukkit.entity.TNTPrimed) wrappedObject).getYield();
    }

    @Override
    public void yield(float yield) {
        ((org.bukkit.entity.TNTPrimed) wrappedObject).setYield(yield);
    }

    @Override
    public boolean isIncendiary() {
        return ((org.bukkit.entity.TNTPrimed) wrappedObject).isIncendiary();
    }

    @Override
    public void isIncendiary(boolean isIncendiary) {
        ((org.bukkit.entity.TNTPrimed) wrappedObject).setIsIncendiary(isIncendiary);
    }
}
