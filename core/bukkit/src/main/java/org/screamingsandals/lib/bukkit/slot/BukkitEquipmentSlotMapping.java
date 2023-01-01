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

import org.bukkit.inventory.EquipmentSlot;
import org.screamingsandals.lib.slot.EquipmentSlotMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;

@Service
public class BukkitEquipmentSlotMapping extends EquipmentSlotMapping {
    public BukkitEquipmentSlotMapping() {
        equipmentSlotConverter
                .registerP2W(EquipmentSlot.class, BukkitEquipmentSlotHolder::new);

        Arrays.stream(EquipmentSlot.values()).forEach(equipmentSlot -> {
            var holder = new BukkitEquipmentSlotHolder(equipmentSlot);
            mapping.put(NamespacedMappingKey.of(equipmentSlot.name()), holder);
            values.add(holder);
        });
    }
}
