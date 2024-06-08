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

package org.screamingsandals.lib.impl.bukkit.slot;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;
import org.screamingsandals.lib.impl.bukkit.item.BukkitItemTypeRegistry1_13;
import org.screamingsandals.lib.impl.bukkit.item.BukkitItemTypeRegistry1_14;
import org.screamingsandals.lib.impl.bukkit.item.BukkitItemTypeRegistry1_8;
import org.screamingsandals.lib.impl.item.ItemTypeRegistry;
import org.screamingsandals.lib.impl.slot.EquipmentSlotGroupRegistry;
import org.screamingsandals.lib.utils.annotations.ServiceFactory;

@UtilityClass
@ServiceFactory
public class BukkitEquipmentSlotGroupRegistryServiceFactory {
    public static @NotNull EquipmentSlotGroupRegistry create() {
        if (BukkitFeature.EQUIPMENT_SLOT_GROUP.isSupported()) {
            return new BukkitEquipmentSlotGroupRegistry1_20_5();
        } else {
            return new BukkitEquipmentSlotGroupRegistry1_8();
        }
    }
}
