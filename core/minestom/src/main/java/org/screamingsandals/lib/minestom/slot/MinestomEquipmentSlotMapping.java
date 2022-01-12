/*
 * Copyright 2022 ScreamingSandals
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

package org.screamingsandals.lib.minestom.slot;

import net.minestom.server.item.attribute.AttributeSlot;
import org.screamingsandals.lib.slot.EquipmentSlotMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;
import java.util.Locale;

@Service
public class MinestomEquipmentSlotMapping extends EquipmentSlotMapping {
    public MinestomEquipmentSlotMapping() {
        equipmentSlotConverter
                .registerP2W(AttributeSlot.class, MinestomEquipmentSlotHolder::new)
                .registerW2P(AttributeSlot.class, equipmentSlotHolder -> AttributeSlot.valueOf(equipmentSlotHolder.platformName()));

        Arrays.stream(AttributeSlot.values()).forEach(equipmentSlot -> {
            final var holder = new MinestomEquipmentSlotHolder(equipmentSlot);
            mapping.put(NamespacedMappingKey.of(equipmentSlot.name().toLowerCase(Locale.ROOT)), holder);
            values.add(holder);
        });
    }
}
