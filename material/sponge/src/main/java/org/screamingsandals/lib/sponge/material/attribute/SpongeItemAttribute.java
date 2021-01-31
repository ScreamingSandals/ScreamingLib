package org.screamingsandals.lib.sponge.material.attribute;

import lombok.Data;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.api.entity.attribute.AttributeModifier;
import org.spongepowered.api.entity.attribute.type.AttributeType;
import org.spongepowered.api.item.inventory.equipment.EquipmentType;

@Data
public class SpongeItemAttribute {
    private final AttributeType attribute;
    private final AttributeModifier attributeModifier;
    @Nullable
    private final EquipmentType equipmentSlot;
}
