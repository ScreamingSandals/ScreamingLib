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

package org.screamingsandals.lib.impl.bukkit.attribute;

import org.bukkit.attribute.AttributeInstance;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.attribute.Attribute;
import org.screamingsandals.lib.attribute.AttributeModifier;
import org.screamingsandals.lib.attribute.AttributeType;
import org.screamingsandals.lib.attribute.ItemAttribute;
import org.screamingsandals.lib.impl.attribute.Attributes;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;
import org.screamingsandals.lib.slot.EquipmentSlot;
import org.screamingsandals.lib.slot.EquipmentSlotGroup;

public class BukkitAttributes1_9 extends Attributes {
    public BukkitAttributes1_9() {
        attributeModifierConverter
                .registerW2P(org.bukkit.attribute.AttributeModifier.class, holder ->
                        new org.bukkit.attribute.AttributeModifier(
                                holder.getUuid(),
                                holder.getName(),
                                holder.getAmount(),
                                org.bukkit.attribute.AttributeModifier.Operation.values()[holder.getOperation().ordinal()]
                        )
                )
                .registerP2W(org.bukkit.attribute.AttributeModifier.class, attributeModifier ->
                        new AttributeModifier(
                                attributeModifier.getUniqueId(),
                                attributeModifier.getName(),
                                attributeModifier.getAmount(),
                                AttributeModifier.Operation.byOrdinal(attributeModifier.getOperation().ordinal())
                        )
                );

        itemAttributeConverter
                .registerW2P(BukkitItemAttribute.class, holder -> {
                    org.bukkit.attribute.AttributeModifier modifier;
                    if (BukkitFeature.EQUIPMENT_SLOT_GROUP.isSupported()) {
                        modifier = new org.bukkit.attribute.AttributeModifier(
                                holder.getUuid(),
                                holder.getName(),
                                holder.getAmount(),
                                org.bukkit.attribute.AttributeModifier.Operation.values()[holder.getOperation().ordinal()],
                                holder.getSlot().as(org.bukkit.inventory.EquipmentSlotGroup.class)
                        );
                    } else {
                        try {
                            modifier = new org.bukkit.attribute.AttributeModifier(
                                    holder.getUuid(),
                                    holder.getName(),
                                    holder.getAmount(),
                                    org.bukkit.attribute.AttributeModifier.Operation.values()[holder.getOperation().ordinal()],
                                    !holder.getSlot().is("any") ? holder.getSlot().as(org.bukkit.inventory.EquipmentSlot.class) : null
                            );
                        } catch (Throwable throwable) {
                            modifier = new org.bukkit.attribute.AttributeModifier(
                                    holder.getUuid(),
                                    holder.getName(),
                                    holder.getAmount(),
                                    org.bukkit.attribute.AttributeModifier.Operation.values()[holder.getOperation().ordinal()]
                            );
                        }
                    }
                    return new BukkitItemAttribute(holder.getType().as(org.bukkit.attribute.Attribute.class), modifier);
                })
                .registerP2W(BukkitItemAttribute.class, bukkitItemAttribute -> {
                    if (BukkitFeature.EQUIPMENT_SLOT_GROUP.isSupported()) {
                        return new ItemAttribute(
                                AttributeType.of(bukkitItemAttribute.getAttribute()),
                                bukkitItemAttribute.getAttributeModifier().getUniqueId(),
                                bukkitItemAttribute.getAttributeModifier().getName(),
                                bukkitItemAttribute.getAttributeModifier().getAmount(),
                                AttributeModifier.Operation.values()[bukkitItemAttribute.getAttributeModifier().getOperation().ordinal()],
                                EquipmentSlotGroup.of(bukkitItemAttribute.getAttributeModifier().getSlotGroup())
                        );
                    }

                    try {
                        return new ItemAttribute(
                                AttributeType.of(bukkitItemAttribute.getAttribute()),
                                bukkitItemAttribute.getAttributeModifier().getUniqueId(),
                                bukkitItemAttribute.getAttributeModifier().getName(),
                                bukkitItemAttribute.getAttributeModifier().getAmount(),
                                AttributeModifier.Operation.values()[bukkitItemAttribute.getAttributeModifier().getOperation().ordinal()],
                                EquipmentSlot.ofNullable(bukkitItemAttribute.getAttributeModifier().getSlot())
                        );
                    } catch (Throwable throwable) {
                        return new ItemAttribute(
                                AttributeType.of(bukkitItemAttribute.getAttribute()),
                                bukkitItemAttribute.getAttributeModifier().getUniqueId(),
                                bukkitItemAttribute.getAttributeModifier().getName(),
                                bukkitItemAttribute.getAttributeModifier().getAmount(),
                                AttributeModifier.Operation.values()[bukkitItemAttribute.getAttributeModifier().getOperation().ordinal()],
                                (EquipmentSlot) null
                        );
                    }
                });
    }

    @Override
    protected @Nullable Attribute wrapAttribute0(@Nullable Object attribute) {
        if (attribute instanceof AttributeInstance) {
            return new BukkitAttribute1_9((AttributeInstance) attribute);
        }
        return null;
    }
}
