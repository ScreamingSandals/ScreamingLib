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

package org.screamingsandals.lib.minestom.material.attribute;

import net.minestom.server.attribute.Attribute;
import net.minestom.server.attribute.AttributeInstance;
import net.minestom.server.attribute.AttributeModifier;
import net.minestom.server.attribute.AttributeOperation;
import net.minestom.server.item.attribute.AttributeSlot;
import net.minestom.server.item.attribute.ItemAttribute;
import org.screamingsandals.lib.attribute.*;
import org.screamingsandals.lib.slot.EquipmentSlotMapping;
import org.screamingsandals.lib.minestom.slot.MinestomEquipmentSlotMapping;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.Optional;

@Service(dependsOn = {
        MinestomAttributeTypeMapping.class,
        MinestomEquipmentSlotMapping.class
})
public class MinestomAttributeMapping extends AttributeMapping {
    public static void init() {
        AttributeMapping.init(MinestomAttributeMapping::new);
    }

    public MinestomAttributeMapping() {
        attributeModifierConverter
                .registerW2P(AttributeModifier.class, holder -> new AttributeModifier(
                        holder.getUuid(),
                        holder.getName(),
                        (float) holder.getAmount(),
                        AttributeOperation.valueOf(holder.getOperation().name())
                ))
                .registerP2W(AttributeModifier.class, attributeModifier -> new AttributeModifierHolder(
                        attributeModifier.getId(),
                        attributeModifier.getName(),
                        attributeModifier.getAmount(),
                        AttributeModifierHolder.Operation.valueOf(attributeModifier.getOperation().name())
                ));

        itemAttributeConverter
                .registerP2W(ItemAttribute.class, itemAttribute -> new ItemAttributeHolder(
                        AttributeTypeMapping.resolve(itemAttribute.getAttribute()).orElseThrow(),
                        itemAttribute.getUuid(),
                        itemAttribute.getInternalName(),
                        itemAttribute.getValue(),
                        AttributeModifierHolder.Operation.valueOf(itemAttribute.getOperation().name()),
                        EquipmentSlotMapping.resolve(itemAttribute.getSlot()).orElse(null) // nullable
                ))
                .registerW2P(ItemAttribute.class, holder -> new ItemAttribute(
                        holder.getUuid(),
                        holder.getName(),
                        holder.getType().as(Attribute.class),
                        AttributeOperation.valueOf(holder.getOperation().name()),
                        holder.getAmount(),
                        holder.getSlot() != null ? holder.getSlot().as(AttributeSlot.class) : null
                ));
    }

    @Override
    protected Optional<AttributeHolder> wrapAttribute0(Object attribute) {
        if (attribute instanceof AttributeInstance) {
            return Optional.of(new MinestomAttributeHolder((AttributeInstance) attribute));
        }
        return Optional.empty();
    }
}
