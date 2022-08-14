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

import org.bukkit.entity.HumanEntity;
import org.screamingsandals.lib.entity.EntityHuman;

public class BukkitEntityHuman extends BukkitEntityLiving implements EntityHuman {
    public BukkitEntityHuman(HumanEntity wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public int getExpToLevel() {
        return ((HumanEntity) wrappedObject).getExpToLevel();
    }

    @Override
    public float getSaturation() {
        try {
            // 1.16.5+
            return ((HumanEntity) wrappedObject).getSaturation();
        } catch (Throwable ignored) {
            // TODO: backport missing api <= 1.16.4
            return 0;
        }
    }

    @Override
    public void setSaturation(float saturation) {
        try {
            // 1.16.5+
            ((HumanEntity) wrappedObject).setSaturation(saturation);
        } catch (Throwable ignored) {
            // TODO: backport missing api <= 1.16.4
        }
    }

    @Override
    public float getExhaustion() {
        try {
            // 1.16.5+
            return ((HumanEntity) wrappedObject).getExhaustion();
        } catch (Throwable ignored) {
            // TODO: backport missing api <= 1.16.4
            return 0;
        }
    }

    @Override
    public void setExhaustion(float exhaustion) {
        try {
            // 1.16.5+
            ((HumanEntity) wrappedObject).setExhaustion(exhaustion);
        } catch (Throwable ignored) {
            // TODO: backport missing api <= 1.16.4
        }
    }

    @Override
    public int getFoodLevel() {
        try {
            // 1.16.5+
            return ((HumanEntity) wrappedObject).getFoodLevel();
        } catch (Throwable ignored) {
            // TODO: backport missing api <= 1.16.4
            return 0;
        }
    }

    @Override
    public void setFoodLevel(int foodLevel) {
        try {
            // 1.16.5+
            ((HumanEntity) wrappedObject).setFoodLevel(foodLevel);
        } catch (Throwable ignored) {
            // TODO: backport missing api <= 1.16.4
        }
    }

    @Override
    public <T> T as(Class<T> type) {
        return super.as(type);
    }
}
