package org.screamingsandals.lib.sponge.material.attribute;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.attribute.AttributeTypeHolder;
import org.screamingsandals.lib.attribute.AttributeTypeMapping;
import org.screamingsandals.lib.sponge.utils.SpongeRegistryMapper;
import org.screamingsandals.lib.utils.key.AttributeMappingKey;
import org.spongepowered.api.entity.attribute.type.AttributeType;
import org.spongepowered.api.registry.RegistryType;
import org.spongepowered.api.registry.RegistryTypes;

public class SpongeAttributeTypeMapping extends AttributeTypeMapping implements SpongeRegistryMapper<AttributeType> {
    public static void init() {
        AttributeTypeMapping.init(SpongeAttributeTypeMapping::new);
    }

    public SpongeAttributeTypeMapping() {
        attributeTypeConverter
                .registerW2P(AttributeType.class, attributeTypeHolder -> getEntry(attributeTypeHolder.getPlatformName()).value())
                .registerP2W(AttributeType.class, attributeType -> new AttributeTypeHolder(getKeyByValue(attributeType).getFormatted()));

        getAllKeys().forEach(key ->
                mapping.put(AttributeMappingKey.of(key.getFormatted()), new AttributeTypeHolder(key.getFormatted()))
        );
    }

    @Override
    @NotNull
    public RegistryType<AttributeType> getRegistryType() {
        return RegistryTypes.ATTRIBUTE_TYPE;
    }
}
