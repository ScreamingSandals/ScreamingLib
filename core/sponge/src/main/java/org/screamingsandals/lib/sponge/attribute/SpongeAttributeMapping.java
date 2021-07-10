package org.screamingsandals.lib.sponge.material.attribute;

import org.screamingsandals.lib.attribute.*;
import org.screamingsandals.lib.material.slot.EquipmentSlotMapping;
import org.screamingsandals.lib.sponge.material.slot.SpongeEquipmentSlotMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.attribute.Attribute;
import org.spongepowered.api.entity.attribute.AttributeModifier;
import org.spongepowered.api.entity.attribute.type.AttributeType;
import org.spongepowered.api.item.inventory.equipment.EquipmentType;
import org.spongepowered.api.registry.RegistryTypes;

import java.util.Optional;

@Service(dependsOn = {
        SpongeAttributeTypeMapping.class,
        SpongeEquipmentSlotMapping.class
})
public class SpongeAttributeMapping extends AttributeMapping {
    public static void init() {
        AttributeMapping.init(SpongeAttributeMapping::new);
    }

    public SpongeAttributeMapping() {
        attributeModifierConverter
                .registerP2W(AttributeModifier.class, attributeModifier -> new AttributeModifierHolder(
                        attributeModifier.getUniqueId(),
                        attributeModifier.getName(),
                        attributeModifier.getAmount(),
                        AttributeModifierHolder.Operation.valueOf(attributeModifier.getOperation().key(RegistryTypes.ATTRIBUTE_OPERATION).getValue().toUpperCase())
                ))
                .registerW2P(AttributeModifier.class, holder -> AttributeModifier.builder()
                        .id(holder.getUuid())
                        .name(holder.getName())
                        .amount(holder.getAmount())
                        .operation(Sponge.getGame().registries().registry(RegistryTypes.ATTRIBUTE_OPERATION).findEntry(ResourceKey.resolve("sponge:" + holder.getOperation().name().toLowerCase())).get().value())
                        .build()
                );

        itemAttributeConverter
                .registerW2P(SpongeItemAttribute.class, holder -> new SpongeItemAttribute(
                        holder.getType().as(AttributeType.class),
                        AttributeModifier.builder()
                                .id(holder.getUuid())
                                .name(holder.getName())
                                .amount(holder.getAmount())
                                .operation(Sponge.getGame().registries().registry(RegistryTypes.ATTRIBUTE_OPERATION).findEntry(ResourceKey.resolve("sponge:" + holder.getOperation().name().toLowerCase())).get().value())
                                .build(),
                        holder.getSlot() != null ? holder.getSlot().as(EquipmentType.class) : null // TODO: check nullability
                ))
                .registerP2W(SpongeItemAttribute.class, spongeItemAttribute -> new ItemAttributeHolder(
                        AttributeTypeMapping.resolve(spongeItemAttribute.getAttribute()).orElseThrow(),
                        spongeItemAttribute.getAttributeModifier().getUniqueId(),
                        spongeItemAttribute.getAttributeModifier().getName(),
                        spongeItemAttribute.getAttributeModifier().getAmount(),
                        AttributeModifierHolder.Operation.valueOf(spongeItemAttribute.getAttributeModifier().getOperation().key(RegistryTypes.ATTRIBUTE_OPERATION).getValue().toUpperCase()),
                        EquipmentSlotMapping.resolve(spongeItemAttribute.getEquipmentSlot()).orElse(null) // nullable
                ));
    }


    @Override
    protected Optional<AttributeHolder> wrapAttribute0(Object attribute) {
        if (attribute instanceof Attribute) {
            return Optional.of(new SpongeAttributeHolder((Attribute) attribute));
        }
        return Optional.empty();
    }
}
