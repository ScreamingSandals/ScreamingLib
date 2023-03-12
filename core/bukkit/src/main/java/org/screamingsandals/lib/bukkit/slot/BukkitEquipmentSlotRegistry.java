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

package org.screamingsandals.lib.bukkit.slot;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.slot.EquipmentSlot;
import org.screamingsandals.lib.slot.EquipmentSlotRegistry;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.ResourceLocation;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;
import org.screamingsandals.lib.utils.registry.SimpleRegistryItemStream;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class BukkitEquipmentSlotRegistry extends EquipmentSlotRegistry {
    // TODO: is there any bukkit-like server supporting custom values for this registry?
    @Override
    protected @Nullable EquipmentSlot resolveMappingPlatform(@NotNull ResourceLocation location) {
        try {
            var value = org.bukkit.inventory.EquipmentSlot.valueOf(location.path().toUpperCase(Locale.ROOT));
            return new BukkitEquipmentSlotType(value);
        } catch (IllegalArgumentException ignored) {
        }
        return null;
    }

    @Override
    protected @NotNull RegistryItemStream<@NotNull EquipmentSlot> getRegistryItemStream0() {
        return new SimpleRegistryItemStream<>(
                () -> Arrays.stream(org.bukkit.inventory.EquipmentSlot.values()),
                BukkitEquipmentSlotType::new,
                equipmentSlot -> ResourceLocation.of(equipmentSlot.name()),
                (equipmentSlot, literal) -> equipmentSlot.name().toLowerCase(Locale.ROOT).contains(literal),
                (equipmentSlot, namespace) -> "minecraft".equals(namespace),
                List.of()
        );
    }
}
