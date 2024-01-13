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

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.entity.HumanEntity;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;

public class BukkitHumanEntity extends BukkitLivingEntity implements HumanEntity {
    public BukkitHumanEntity(@NotNull org.bukkit.entity.HumanEntity wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public int getExpToLevel() {
        return ((org.bukkit.entity.HumanEntity) wrappedObject).getExpToLevel();
    }

    @Override
    public float getSaturation() {
        if (BukkitFeature.HUMAN_SATURATION.isSupported()) {
            // 1.16.5+
            return ((org.bukkit.entity.HumanEntity) wrappedObject).getSaturation();
        } else {
            if (wrappedObject instanceof Player) {
                return ((org.bukkit.entity.Player) wrappedObject).getSaturation();
            }
            return 0;
        }
    }

    @Override
    public void setSaturation(float saturation) {
        if (BukkitFeature.HUMAN_SATURATION.isSupported()) {
            // 1.16.5+
            ((org.bukkit.entity.HumanEntity) wrappedObject).setSaturation(saturation);
        } else {
            if (wrappedObject instanceof Player) {
                ((org.bukkit.entity.Player) wrappedObject).setSaturation(saturation);
            }
        }
    }

    @Override
    public float getExhaustion() {
        if (BukkitFeature.HUMAN_EXHAUSTION.isSupported()) {
            // 1.16.5+
            return ((org.bukkit.entity.HumanEntity) wrappedObject).getExhaustion();
        } else {
            if (wrappedObject instanceof Player) {
                return ((org.bukkit.entity.Player) wrappedObject).getExhaustion();
            }
            return 0;
        }
    }

    @Override
    public void setExhaustion(float exhaustion) {
        if (BukkitFeature.HUMAN_EXHAUSTION.isSupported()) {
            // 1.16.5+
            ((org.bukkit.entity.HumanEntity) wrappedObject).setExhaustion(exhaustion);
        } else  {
            if (wrappedObject instanceof Player) {
                ((org.bukkit.entity.Player) wrappedObject).setExhaustion(exhaustion);
            }
        }
    }

    @Override
    public int getFoodLevel() {
        if (BukkitFeature.FOOD_LEVEL.isSupported()) {
            // 1.16.5+
            return ((org.bukkit.entity.HumanEntity) wrappedObject).getFoodLevel();
        } else {
            if (wrappedObject instanceof Player) {
                return ((org.bukkit.entity.Player) wrappedObject).getFoodLevel();
            }
            return 0;
        }
    }

    @Override
    public void setFoodLevel(int foodLevel) {
        if (BukkitFeature.FOOD_LEVEL.isSupported()) {
            // 1.16.5+
            ((org.bukkit.entity.HumanEntity) wrappedObject).setFoodLevel(foodLevel);
        } else {
            if (wrappedObject instanceof Player) {
                ((org.bukkit.entity.Player) wrappedObject).setFoodLevel(foodLevel);
            }
        }
    }
}
