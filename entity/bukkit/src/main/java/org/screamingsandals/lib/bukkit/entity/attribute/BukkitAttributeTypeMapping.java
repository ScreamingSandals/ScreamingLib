package org.screamingsandals.lib.bukkit.entity.attribute;

import org.bukkit.attribute.Attribute;
import org.screamingsandals.lib.entity.attribute.AttributeTypeHolder;
import org.screamingsandals.lib.entity.attribute.AttributeTypeMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.AttributeMappingKey;

import java.util.Arrays;

@Service
public class BukkitAttributeTypeMapping extends AttributeTypeMapping {
    public static void init() {
        AttributeTypeMapping.init(BukkitAttributeTypeMapping::new);
    }

    public BukkitAttributeTypeMapping() {
        attributeTypeConverter
                .registerP2W(Attribute.class, entityType -> new AttributeTypeHolder(entityType.name()))
                .registerW2P(Attribute.class, entityTypeHolder -> Attribute.valueOf(entityTypeHolder.getPlatformName()));

        Arrays.stream(Attribute.values()).forEach(entityType -> mapping.put(AttributeMappingKey.of(entityType.name()), new AttributeTypeHolder(entityType.name())));
    }
}
