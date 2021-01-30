package org.screamingsandals.lib.sponge.material.attribute;

import org.screamingsandals.lib.material.attribute.AttributeHolder;
import org.screamingsandals.lib.material.attribute.AttributeMapping;
import org.screamingsandals.lib.material.attribute.AttributeModifierHolder;
import org.screamingsandals.lib.utils.annotations.Service;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.attribute.AttributeModifier;
import org.spongepowered.api.registry.RegistryTypes;

import java.util.Optional;

@Service
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
                        AttributeModifierHolder.Operation.valueOf(attributeModifier.getOperation().key(RegistryTypes.ATTRIBUTE_OPERATION).getValue().toUpperCase()),
                        null
                ))
                .registerW2P(AttributeModifier.class, holder -> AttributeModifier.builder()
                        .id(holder.getUuid())
                        .name(holder.getName())
                        .amount(holder.getAmount())
                        .operation(Sponge.getGame().registries().registry(RegistryTypes.ATTRIBUTE_OPERATION).findEntry(ResourceKey.resolve("sponge:" + holder.getOperation().name().toLowerCase())).get().value())
                        .build()
                );
    }


    @Override
    protected Optional<AttributeHolder> wrapAttribute0(Object attribute) {
        return Optional.empty();
    }
}
