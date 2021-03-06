package org.screamingsandals.lib.minestom.material.attribute;

import net.minestom.server.attribute.Attribute;
import org.screamingsandals.lib.attribute.AttributeTypeHolder;
import org.screamingsandals.lib.attribute.AttributeTypeMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.AttributeMappingKey;

import java.util.Arrays;

@Service
public class MinestomAttributeTypeMapping extends AttributeTypeMapping {
    public static void init() {
        AttributeTypeMapping.init(MinestomAttributeTypeMapping::new);
    }

    public MinestomAttributeTypeMapping() {
        attributeTypeConverter
                .registerP2W(Attribute.class, entityType -> new AttributeTypeHolder(entityType.getKey()))
                .registerW2P(Attribute.class, entityTypeHolder -> Attribute.fromKey(entityTypeHolder.getPlatformName()));

        Arrays.stream(Attribute.values()).forEach(attributeType -> mapping.put(AttributeMappingKey.of(attributeType.getKey()), new AttributeTypeHolder(attributeType.getKey())));
    }
}
