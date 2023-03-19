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

package org.screamingsandals.lib.bukkit.attribute;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.attribute.*;
import org.screamingsandals.lib.slot.EquipmentSlot;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.utils.annotations.Service;

@Service
public class BukkitAttributeMapping extends AttributeMapping {
    public BukkitAttributeMapping() {
        if (Version.isVersion(1, 9)) {
            attributeModifierConverter
                    .registerW2P(AttributeModifier.class, holder ->
                            new AttributeModifier(
                                    holder.getUuid(),
                                    holder.getName(),
                                    holder.getAmount(),
                                    AttributeModifier.Operation.values()[holder.getOperation().ordinal()]
                            )
                    )
                    .registerP2W(AttributeModifier.class, attributeModifier ->
                            new AttributeModifierHolder(
                                    attributeModifier.getUniqueId(),
                                    attributeModifier.getName(),
                                    attributeModifier.getAmount(),
                                    AttributeModifierHolder.Operation.byOrdinal(attributeModifier.getOperation().ordinal())
                            )
                    );

            itemAttributeConverter
                    .registerW2P(BukkitItemAttribute.class, holder -> {
                        AttributeModifier modifier;
                        try {
                            modifier = new AttributeModifier(
                                    holder.getUuid(),
                                    holder.getName(),
                                    holder.getAmount(),
                                    AttributeModifier.Operation.values()[holder.getOperation().ordinal()],
                                    holder.getSlot() != null ? holder.getSlot().as(org.bukkit.inventory.EquipmentSlot.class) : null
                            );
                        } catch (Throwable throwable) {
                            modifier = new AttributeModifier(
                                    holder.getUuid(),
                                    holder.getName(),
                                    holder.getAmount(),
                                    AttributeModifier.Operation.values()[holder.getOperation().ordinal()]
                            );
                        }
                        return new BukkitItemAttribute(holder.getType().as(Attribute.class), modifier);
                    })
                    .registerP2W(BukkitItemAttribute.class, bukkitItemAttribute -> {
                        try {
                            return new ItemAttributeHolder(
                                    AttributeTypeHolder.of(bukkitItemAttribute.getAttribute()),
                                    bukkitItemAttribute.getAttributeModifier().getUniqueId(),
                                    bukkitItemAttribute.getAttributeModifier().getName(),
                                    bukkitItemAttribute.getAttributeModifier().getAmount(),
                                    AttributeModifierHolder.Operation.values()[bukkitItemAttribute.getAttributeModifier().getOperation().ordinal()],
                                    EquipmentSlot.ofNullable(bukkitItemAttribute.getAttributeModifier().getOperation())
                            );
                        } catch (Throwable throwable) {
                            return new ItemAttributeHolder(
                                    AttributeTypeHolder.of(bukkitItemAttribute.getAttribute()),
                                    bukkitItemAttribute.getAttributeModifier().getUniqueId(),
                                    bukkitItemAttribute.getAttributeModifier().getName(),
                                    bukkitItemAttribute.getAttributeModifier().getAmount(),
                                    AttributeModifierHolder.Operation.values()[bukkitItemAttribute.getAttributeModifier().getOperation().ordinal()],
                                    null
                            );
                        }
                    });
        }
    }

    @Override
    protected @Nullable AttributeHolder wrapAttribute0(@Nullable Object attribute) {
        if (attribute instanceof AttributeInstance) {
            return new BukkitAttributeHolder((AttributeInstance) attribute);
        }
        return null;
    }
}
