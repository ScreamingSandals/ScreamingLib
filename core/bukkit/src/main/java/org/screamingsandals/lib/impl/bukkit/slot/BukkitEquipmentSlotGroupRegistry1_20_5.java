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

package org.screamingsandals.lib.impl.bukkit.slot;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.slot.EquipmentSlotGroupRegistry;
import org.screamingsandals.lib.impl.utils.registry.SimpleRegistryItemStream;
import org.screamingsandals.lib.slot.EquipmentSlotGroup;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BukkitEquipmentSlotGroupRegistry1_20_5 extends EquipmentSlotGroupRegistry {
    public BukkitEquipmentSlotGroupRegistry1_20_5() {
        specialType(org.bukkit.inventory.EquipmentSlotGroup.class, BukkitEquipmentSlotGroup1_20_5::new);
    }

    @Override
    protected @Nullable EquipmentSlotGroup resolveMappingPlatform(@NotNull ResourceLocation location) {
        if (!"minecraft".equals(location.namespace())) {
            return null;
        }

        var value = org.bukkit.inventory.EquipmentSlotGroup.getByName(location.path().toUpperCase(Locale.ROOT));
        if (value != null) {
            return new BukkitEquipmentSlotGroup1_20_5(value);
        }

        return null;
    }

    @Override
    protected @NotNull RegistryItemStream<@NotNull EquipmentSlotGroup> getRegistryItemStream0() {
        @SuppressWarnings("unchecked")
        @NotNull Map<@NotNull String, org.bukkit.inventory.EquipmentSlotGroup> map =
                (Map<String, org.bukkit.inventory.EquipmentSlotGroup>) Reflect.getField(org.bukkit.inventory.EquipmentSlotGroup.class, "BY_NAME");
        return new SimpleRegistryItemStream<>(
                map.values()::stream,
                BukkitEquipmentSlotGroup1_20_5::new,
                group -> ResourceLocation.of(group.toString()),
                (group, literal) -> group.toString().toLowerCase(Locale.ROOT).contains(literal),
                (group, namespace) -> "minecraft".equals(namespace),
                List.of()
        );
    }
}
