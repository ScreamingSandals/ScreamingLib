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

package org.screamingsandals.lib.impl.bukkit.item;

import org.bukkit.Material;
import org.bukkit.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bukkit.utils.BukkitRegistry;
import org.screamingsandals.lib.item.ItemType;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;

public class BukkitItemTypeRegistry1_14 extends BukkitItemTypeRegistry1_13 {
    @Override
    protected @Nullable ItemType resolveMappingPlatform(@NotNull ResourceLocation location) {
        return BukkitRegistry.tryObtainItem(Registry.MATERIAL, BukkitItemType1_13::new, location, Material::isItem);
    }

    @Override
    protected @NotNull RegistryItemStream<@NotNull ItemType> getRegistryItemStream0() {
        return BukkitRegistry.registryStream(Registry.MATERIAL, BukkitItemType1_13::new, Material::isItem);
    }
}
