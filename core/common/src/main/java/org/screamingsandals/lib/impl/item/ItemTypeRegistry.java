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

package org.screamingsandals.lib.impl.item;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.ItemBlockIdsRemapper;
import org.screamingsandals.lib.item.ItemType;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.ProvidedService;
import org.screamingsandals.lib.impl.utils.registry.SimpleRegistry;

import java.util.Map;

@ProvidedService
@ApiStatus.Internal
public abstract class ItemTypeRegistry extends SimpleRegistry<ItemType> {
    private static @Nullable ItemTypeRegistry registry;
    private static @Nullable ItemType cachedAir;

    public ItemTypeRegistry() {
        super(ItemType.class);
        Preconditions.checkArgument(registry == null, "ItemTypeRegistry is already initialized!");
        registry = this;
    }

    public static @NotNull ItemTypeRegistry getInstance() {
        return Preconditions.checkNotNull(registry, "ItemTypeRegistry is not initialized yet!");
    }

    public static @NotNull ItemType colorize(@NotNull ItemType holder, @NotNull String color) {
        Preconditions.checkNotNull(registry, "ItemTypeRegistry is not initialized yet!");
        return ItemBlockIdsRemapper.colorableItems.entrySet().stream()
                .filter(c -> c.getKey().test(holder))
                .map(Map.Entry::getValue)
                .findFirst()
                .flatMap(fun -> fun.apply(color))
                .orElse(holder);
    }

    @Override
    public void mapAlias(@NotNull String mappingKey, @NotNull String alias) {
        super.mapAlias(mappingKey, alias);
    }

    public static @NotNull ItemType getCachedAir() {
        if (cachedAir == null) {
            cachedAir = Preconditions.checkNotNull(registry, "ItemTypeRegistry is not initialized yet!").resolveMapping("minecraft:air");
            Preconditions.checkNotNullIllegal(cachedAir, "Could not find item type: minecraft:air");
        }
        return cachedAir;
    }
}
