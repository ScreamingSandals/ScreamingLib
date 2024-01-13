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

package org.screamingsandals.lib.impl.bukkit.entity.villager;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.entity.villager.Profession;
import org.screamingsandals.lib.entity.villager.Villager;
import org.screamingsandals.lib.entity.villager.VillagerType;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;
import org.screamingsandals.lib.impl.bukkit.entity.BukkitPathfinderMob;

public class BukkitVillager extends BukkitPathfinderMob implements Villager {
    public BukkitVillager(@NotNull org.bukkit.entity.Villager wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull Profession profession() {
        return Profession.of(((org.bukkit.entity.Villager) wrappedObject).getProfession());
    }

    @Override
    public void profession(@NotNull Profession profession) {
        ((org.bukkit.entity.Villager) wrappedObject).setProfession(profession.as(org.bukkit.entity.Villager.Profession.class));
    }

    @Override
    public @NotNull VillagerType villagerType() {
        if (BukkitFeature.NEW_VILLAGERS.isSupported()) {
            return VillagerType.of(((org.bukkit.entity.Villager) wrappedObject).getVillagerType());
        }
        return BukkitVillagerTypeRegistry1_8.INSTANCE;
    }

    @Override
    public void villagerType(@NotNull VillagerType villagerType) {
        if (BukkitFeature.NEW_VILLAGERS.isSupported()) {
            ((org.bukkit.entity.Villager) wrappedObject).setVillagerType(villagerType.as(org.bukkit.entity.Villager.Type.class));
        }
    }

    @Override
    public boolean baby() {
        return !((org.bukkit.entity.Villager) wrappedObject).isAdult();
    }

    @Override
    public void baby(boolean isBaby) {
        if (isBaby) {
            ((org.bukkit.entity.Villager) wrappedObject).setBaby();
        } else {
            ((org.bukkit.entity.Villager) wrappedObject).setAdult();
        }
    }

    @Override
    public int age() {
        return ((org.bukkit.entity.Villager) wrappedObject).getAge();
    }

    @Override
    public void age(int age) {
        ((org.bukkit.entity.Villager) wrappedObject).setAge(age);
    }
}
