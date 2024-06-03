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

import org.bukkit.Registry;
import org.bukkit.entity.Villager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.villager.VillagerType;
import org.screamingsandals.lib.impl.bukkit.utils.BukkitRegistry;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;

public class BukkitVillagerTypeRegistry1_14 extends BukkitVillagerTypeRegistry {
    public BukkitVillagerTypeRegistry1_14() {
        specialType(Villager.Type.class, BukkitVillagerType1_14::new);
    }

    @Override
    protected @NotNull RegistryItemStream<@NotNull VillagerType> getRegistryItemStream0() {
        return BukkitRegistry.registryStream(Registry.VILLAGER_TYPE, BukkitVillagerType1_14::new);
    }

    @Override
    protected @Nullable VillagerType resolveMappingPlatform(@NotNull ResourceLocation location) {
        return BukkitRegistry.tryObtainItem(Registry.VILLAGER_TYPE, BukkitVillagerType1_14::new, location);
    }
}
