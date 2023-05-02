/*
 * Copyright 2023 ScreamingSandals
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

package org.screamingsandals.lib.impl.bukkit.container.type;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.container.type.InventoryType;
import org.screamingsandals.lib.impl.container.type.InventoryTypeRegistry;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;
import org.screamingsandals.lib.impl.utils.registry.SimpleRegistryItemStream;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class BukkitInventoryTypeRegistry extends InventoryTypeRegistry {
    // TODO: Bukkit's inventory types doesn't exactly match the vanilla types, we should make correct translations

    public BukkitInventoryTypeRegistry() {
        specialType(org.bukkit.event.inventory.InventoryType.class, BukkitInventoryType::new);
    }

    @Override
    protected @NotNull RegistryItemStream<@NotNull InventoryType> getRegistryItemStream0() {
        return new SimpleRegistryItemStream<>(
                () -> Arrays.stream(org.bukkit.event.inventory.InventoryType.values()),
                BukkitInventoryType::new,
                inventoryType -> ResourceLocation.of(inventoryType.name()),
                (inventoryType, literal) -> inventoryType.name().toLowerCase(Locale.ROOT).contains(literal),
                (inventoryType, namespace) -> "minecraft".equals(namespace),
                List.of()
        );
    }

    @Override
    protected @Nullable InventoryType resolveMappingPlatform(@NotNull ResourceLocation location) {
        if (!"minecraft".equals(location.namespace())) {
            return null;
        }

        try {
            var value = org.bukkit.event.inventory.InventoryType.valueOf(location.path().toUpperCase(Locale.ROOT));
            return new BukkitInventoryType(value);
        } catch (IllegalArgumentException ignored) {
        }

        return null;
    }
}
