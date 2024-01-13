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

package org.screamingsandals.lib.impl.bukkit.entity.monster.zombie;

import org.bukkit.entity.Villager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.monster.zombie.ZombieVillager;
import org.screamingsandals.lib.entity.villager.Profession;
import org.screamingsandals.lib.entity.villager.VillagerType;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;
import org.screamingsandals.lib.impl.bukkit.entity.villager.BukkitVillagerTypeRegistry1_8;

public class BukkitZombieVillager1_11 extends BukkitZombie implements ZombieVillager {
    public BukkitZombieVillager1_11(@NotNull org.bukkit.entity.ZombieVillager wrappedObject) {
        super(wrappedObject);
    }

    @SuppressWarnings("ConstantValue") // idea is on drugs or something
    @Override
    public @Nullable Profession profession() {
        var profession = ((org.bukkit.entity.ZombieVillager) wrappedObject).getVillagerProfession();
        return profession != null ? Profession.of(profession) : null;
    }

    @Override
    public void profession(@Nullable Profession profession) {
        ((org.bukkit.entity.ZombieVillager) wrappedObject).setVillagerProfession(profession != null ? profession.as(Villager.Profession.class) : null);
    }

    @Override
    public @NotNull VillagerType villagerType() {
        if (BukkitFeature.NEW_VILLAGERS.isSupported()) {
            return VillagerType.of(((org.bukkit.entity.ZombieVillager) wrappedObject).getVillagerType());
        }
        return BukkitVillagerTypeRegistry1_8.INSTANCE;
    }

    @Override
    public void villagerType(@NotNull VillagerType villagerType) {
        if (BukkitFeature.NEW_VILLAGERS.isSupported()) {
            ((org.bukkit.entity.ZombieVillager) wrappedObject).setVillagerType(villagerType.as(Villager.Type.class));
        }
    }
}
