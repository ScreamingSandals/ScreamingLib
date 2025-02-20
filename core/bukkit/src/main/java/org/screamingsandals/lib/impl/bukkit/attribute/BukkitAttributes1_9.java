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

package org.screamingsandals.lib.impl.bukkit.attribute;

import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.attribute.Attribute;
import org.screamingsandals.lib.attribute.AttributeModifier;
import org.screamingsandals.lib.attribute.AttributeType;
import org.screamingsandals.lib.attribute.ItemAttribute;
import org.screamingsandals.lib.impl.attribute.Attributes;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;
import org.screamingsandals.lib.impl.bukkit.compat.v1_20_6.AttributeModifierConversion;
import org.screamingsandals.lib.utils.ResourceLocation;

public class BukkitAttributes1_9 extends Attributes {
    public BukkitAttributes1_9() {
        if (BukkitFeature.ATTRIBUTE_MODIFIER_KEYED.isSupported()) {
            attributeModifierConverter
                    .registerW2P(org.bukkit.attribute.AttributeModifier.class, modifier -> new org.bukkit.attribute.AttributeModifier(
                            new NamespacedKey(modifier.getLocation().namespace(), modifier.getLocation().path()),
                            modifier.getAmount(),
                            org.bukkit.attribute.AttributeModifier.Operation.values()[modifier.getOperation().ordinal()],
                            EquipmentSlotGroup.ANY // EquipmentSlotGroup is redundant for attribute modifier, Bukkit API is fucked and combines two classes together
                    ))
                    .registerP2W(org.bukkit.attribute.AttributeModifier.class, modifier -> new AttributeModifier(
                            ResourceLocation.of(modifier.getKey().getNamespace(), modifier.getKey().getKey()),
                            modifier.getAmount(),
                            AttributeModifier.Operation.byOrdinal(modifier.getOperation().ordinal())
                    ));

            itemAttributeConverter
                    .registerW2P(BukkitItemAttribute.class, holder ->
                            new BukkitItemAttribute(holder.getType().as(org.bukkit.attribute.Attribute.class), new org.bukkit.attribute.AttributeModifier(
                                    new NamespacedKey(holder.getLocation().namespace(), holder.getLocation().path()),
                                    holder.getAmount(),
                                    org.bukkit.attribute.AttributeModifier.Operation.values()[holder.getOperation().ordinal()],
                                    holder.getSlot().as(EquipmentSlotGroup.class)
                            ))
                    )
                    .registerP2W(BukkitItemAttribute.class, bukkitItemAttribute -> {
                        var attributeModifier = bukkitItemAttribute.getAttributeModifier();
                        return new ItemAttribute(
                                AttributeType.of(bukkitItemAttribute.getAttribute()),
                                ResourceLocation.of(attributeModifier.getKey().getNamespace(), attributeModifier.getKey().getKey()),
                                attributeModifier.getAmount(),
                                AttributeModifier.Operation.byOrdinal(attributeModifier.getOperation().ordinal()),
                                org.screamingsandals.lib.slot.EquipmentSlotGroup.of(attributeModifier.getSlotGroup())
                        );
                    });
        } else {
            attributeModifierConverter
                    .registerW2P(org.bukkit.attribute.AttributeModifier.class, AttributeModifierConversion::constructBukkitModifier)
                    .registerP2W(org.bukkit.attribute.AttributeModifier.class, AttributeModifierConversion::constructSlibModifier);

            itemAttributeConverter
                    .registerW2P(BukkitItemAttribute.class, holder -> new BukkitItemAttribute(holder.getType().as(org.bukkit.attribute.Attribute.class), AttributeModifierConversion.constructBukkitItemModifier(holder)))
                    .registerP2W(BukkitItemAttribute.class, bukkitItemAttribute -> AttributeModifierConversion.constructSlibItemModifier(AttributeType.of(bukkitItemAttribute.getAttribute()), bukkitItemAttribute.getAttributeModifier()));
        }
    }

    @Override
    protected @Nullable Attribute wrapAttribute0(@Nullable Object attribute) {
        if (attribute instanceof AttributeInstance) {
            return new BukkitAttribute1_9((AttributeInstance) attribute);
        }
        return null;
    }
}
