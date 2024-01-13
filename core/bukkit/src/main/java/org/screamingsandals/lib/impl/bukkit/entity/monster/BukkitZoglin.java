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

package org.screamingsandals.lib.impl.bukkit.entity.monster;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.entity.monster.Zoglin;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;

public class BukkitZoglin extends BukkitMonster implements Zoglin {
    public BukkitZoglin(@NotNull org.bukkit.entity.Zoglin wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public boolean baby() {
        if (BukkitFeature.ENTITY_ZOMBIE_PIGLIN_ZOGLIN_EXTENDS_AGEABLE.isSupported()) {
            return !((org.bukkit.entity.Zoglin) wrappedObject).isAdult();
        }

        //noinspection deprecation
        return ((org.bukkit.entity.Zoglin) wrappedObject).isBaby();
    }

    @Override
    public void baby(boolean isBaby) {
        if (BukkitFeature.ENTITY_ZOMBIE_PIGLIN_ZOGLIN_EXTENDS_AGEABLE.isSupported()) {
            if (isBaby) {
                ((org.bukkit.entity.Zoglin) wrappedObject).setBaby();
            } else {
                ((org.bukkit.entity.Zoglin) wrappedObject).setAdult();
            }
            return;
        }

        //noinspection deprecation
        ((org.bukkit.entity.Zoglin) wrappedObject).setBaby(isBaby);
    }

    @Override
    public int age() {
        if (BukkitFeature.ENTITY_ZOMBIE_PIGLIN_ZOGLIN_EXTENDS_AGEABLE.isSupported()) {
            return ((org.bukkit.entity.Zoglin) wrappedObject).getAge();
        }

        return baby() ? -1 : 0;
    }

    @Override
    public void age(int age) {
        if (BukkitFeature.ENTITY_ZOMBIE_PIGLIN_ZOGLIN_EXTENDS_AGEABLE.isSupported()) {
            ((org.bukkit.entity.Zoglin) wrappedObject).setAge(age);
            return;
        }
        baby(age < 0);
    }
}
