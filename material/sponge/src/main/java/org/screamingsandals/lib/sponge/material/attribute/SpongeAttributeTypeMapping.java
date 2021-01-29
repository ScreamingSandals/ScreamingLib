package org.screamingsandals.lib.sponge.material.attribute;

import org.screamingsandals.lib.material.attribute.AttributeTypeHolder;
import org.screamingsandals.lib.material.attribute.AttributeTypeMapping;
import org.screamingsandals.lib.utils.key.AttributeMappingKey;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.attribute.type.AttributeType;
import org.spongepowered.api.registry.RegistryTypes;

public class SpongeAttributeTypeMapping extends AttributeTypeMapping {
    public static void init() {
        AttributeTypeMapping.init(SpongeAttributeTypeMapping::new);
    }

    public SpongeAttributeTypeMapping() {
        attributeTypeConverter
                .registerW2P(AttributeType.class, attributeTypeHolder -> Sponge.getGame().registries().registry(RegistryTypes.ATTRIBUTE_TYPE).findEntry(ResourceKey.resolve(attributeTypeHolder.getPlatformName())).orElseThrow().value())
                .registerP2W(AttributeType.class, attributeType -> new AttributeTypeHolder(Sponge.getGame().registries().registry(RegistryTypes.ATTRIBUTE_TYPE).findValueKey(attributeType).orElseThrow().getFormatted()));

        Sponge.getGame().registries().registry(RegistryTypes.ATTRIBUTE_TYPE).forEach(attributeType ->
                mapping.put(AttributeMappingKey.of(attributeType.key().getFormatted()), new AttributeTypeHolder(attributeType.key().getFormatted()))
        );
    }
}
