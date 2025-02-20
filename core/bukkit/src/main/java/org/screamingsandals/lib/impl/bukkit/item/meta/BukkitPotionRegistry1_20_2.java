/*
 * Copyright 2025 ScreamingSandals
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

package org.screamingsandals.lib.impl.bukkit.item.meta;

import org.bukkit.Registry;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bukkit.utils.BukkitRegistry;
import org.screamingsandals.lib.impl.item.meta.PotionRegistry;
import org.screamingsandals.lib.item.meta.Potion;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;

public class BukkitPotionRegistry1_20_2 extends PotionRegistry {
    public BukkitPotionRegistry1_20_2() {
        specialType(PotionType.class, BukkitPotion1_20_2::new);
    }

    @Override
    protected @Nullable Potion resolveMappingPlatform(@NotNull ResourceLocation location) {
        return BukkitRegistry.tryObtainItem(Registry.POTION, BukkitPotion1_20_2::new, location);
    }

    @Override
    protected @NotNull RegistryItemStream<@NotNull Potion> getRegistryItemStream0() {
        return BukkitRegistry.registryStream(Registry.POTION, BukkitPotion1_20_2::new);
    }
}
