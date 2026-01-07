/*
 * Copyright 2026 ScreamingSandals
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

package org.screamingsandals.lib.impl.bukkit.compat.v1_20_6;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.attribute.AttributeModifier;
import org.screamingsandals.lib.attribute.AttributeType;
import org.screamingsandals.lib.attribute.ItemAttribute;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;
import org.screamingsandals.lib.slot.EquipmentSlot;
import org.screamingsandals.lib.slot.EquipmentSlotGroup;
import org.screamingsandals.lib.utils.Preconditions;

@UtilityClass
public class AttributeModifierConversion {
    public static @NotNull AttributeModifier constructSlibModifier(@NotNull org.bukkit.attribute.AttributeModifier attributeModifier) {
        return new AttributeModifier(
                attributeModifier.getUniqueId(),
                attributeModifier.getName(),
                attributeModifier.getAmount(),
                AttributeModifier.Operation.byOrdinal(attributeModifier.getOperation().ordinal())
        );
    }

    public static @NotNull org.bukkit.attribute.AttributeModifier constructBukkitModifier(@NotNull AttributeModifier modifier) {
        Preconditions.checkNotNull(modifier.getUuid() != null);
        Preconditions.checkNotNull(modifier.getName() != null);
        
        return new org.bukkit.attribute.AttributeModifier(
                modifier.getUuid(),
                modifier.getName(),
                modifier.getAmount(),
                org.bukkit.attribute.AttributeModifier.Operation.values()[modifier.getOperation().ordinal()]
        );
    }
    
    public static @NotNull org.bukkit.attribute.AttributeModifier constructBukkitItemModifier(@NotNull ItemAttribute modifier) {
        Preconditions.checkNotNull(modifier.getUuid() != null);
        Preconditions.checkNotNull(modifier.getName() != null);
        
        if (BukkitFeature.EQUIPMENT_SLOT_GROUP.isSupported()) {
            return new org.bukkit.attribute.AttributeModifier(
                    modifier.getUuid(),
                    modifier.getName(),
                    modifier.getAmount(),
                    org.bukkit.attribute.AttributeModifier.Operation.values()[modifier.getOperation().ordinal()],
                    modifier.getSlot().as(org.bukkit.inventory.EquipmentSlotGroup.class)
            );
        } else {
            try {
                return new org.bukkit.attribute.AttributeModifier(
                        modifier.getUuid(),
                        modifier.getName(),
                        modifier.getAmount(),
                        org.bukkit.attribute.AttributeModifier.Operation.values()[modifier.getOperation().ordinal()],
                        !modifier.getSlot().is("any") ? modifier.getSlot().as(org.bukkit.inventory.EquipmentSlot.class) : null
                );
            } catch (Throwable throwable) {
                return new org.bukkit.attribute.AttributeModifier(
                        modifier.getUuid(),
                        modifier.getName(),
                        modifier.getAmount(),
                        org.bukkit.attribute.AttributeModifier.Operation.values()[modifier.getOperation().ordinal()]
                );
            }
        }
    }
    
    public static @NotNull ItemAttribute constructSlibItemModifier(@NotNull AttributeType attributeType, @NotNull org.bukkit.attribute.AttributeModifier modifier) {
        if (BukkitFeature.EQUIPMENT_SLOT_GROUP.isSupported()) {
            return new ItemAttribute(
                    attributeType,
                    modifier.getUniqueId(),
                    modifier.getName(),
                    modifier.getAmount(),
                    AttributeModifier.Operation.values()[modifier.getOperation().ordinal()],
                    EquipmentSlotGroup.of(modifier.getSlotGroup())
            );
        }

        try {
            return new ItemAttribute(
                    attributeType,
                    modifier.getUniqueId(),
                    modifier.getName(),
                    modifier.getAmount(),
                    AttributeModifier.Operation.values()[modifier.getOperation().ordinal()],
                    EquipmentSlot.ofNullable(modifier.getSlot())
            );
        } catch (Throwable throwable) {
            return new ItemAttribute(
                    attributeType,
                    modifier.getUniqueId(),
                    modifier.getName(),
                    modifier.getAmount(),
                    AttributeModifier.Operation.values()[modifier.getOperation().ordinal()],
                    (EquipmentSlot) null
            );
        }
    }
}
