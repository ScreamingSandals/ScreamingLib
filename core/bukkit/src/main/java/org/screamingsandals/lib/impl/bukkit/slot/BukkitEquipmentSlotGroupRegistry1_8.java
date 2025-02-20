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
import org.screamingsandals.lib.utils.registry.RegistryItemStream;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

public class BukkitEquipmentSlotGroupRegistry1_8 extends EquipmentSlotGroupRegistry {
    public BukkitEquipmentSlotGroupRegistry1_8() {
        specialType(org.bukkit.inventory.EquipmentSlot.class, BukkitEquipmentSlotGroup1_8::new);

        // Vanilla <-> Bukkit
        mapAlias("MAINHAND", "HAND");
        mapAlias("OFFHAND", "OFF_HAND");
    }

    @Override
    protected @Nullable EquipmentSlotGroup resolveMappingPlatform(@NotNull ResourceLocation location) {
        if (!"minecraft".equals(location.namespace())) {
            return null;
        }

        if ("any".equals(location.path())) {
            return BukkitEquipmentSlotGroupAny1_8.INSTANCE;
        }

        try {
            var value = org.bukkit.inventory.EquipmentSlot.valueOf(location.path().toUpperCase(Locale.ROOT));
            return new BukkitEquipmentSlotGroup1_8(value);
        } catch (IllegalArgumentException ignored) {
        }
        return null;
    }

    @Override
    protected @NotNull RegistryItemStream<@NotNull EquipmentSlotGroup> getRegistryItemStream0() {
        return new SimpleRegistryItemStream<>(
                () -> Stream.concat(Stream.of(BukkitEquipmentSlotGroupAny1_8.INSTANCE), Arrays.stream(org.bukkit.inventory.EquipmentSlot.values())),
                obj -> obj instanceof org.bukkit.inventory.EquipmentSlot ? new BukkitEquipmentSlotGroup1_8((org.bukkit.inventory.EquipmentSlot) obj) : (EquipmentSlotGroup) obj,
                obj -> ResourceLocation.of(obj instanceof org.bukkit.inventory.EquipmentSlot ? ((org.bukkit.inventory.EquipmentSlot) obj).name() : "any"),
                (obj, literal) -> (obj instanceof org.bukkit.inventory.EquipmentSlot ? ((org.bukkit.inventory.EquipmentSlot) obj).name().toLowerCase(Locale.ROOT) : "any").contains(literal),
                (obj, namespace) -> "minecraft".equals(namespace),
                List.of()
        );
    }
}
