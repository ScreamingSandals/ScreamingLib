package org.screamingsandals.lib.bukkit.attribute;

import org.bukkit.attribute.Attribute;
import org.screamingsandals.lib.attribute.AttributeTypeHolder;
import org.screamingsandals.lib.attribute.AttributeTypeMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.AttributeMappingKey;

import java.util.Arrays;

@Service
public class BukkitAttributeTypeMapping extends AttributeTypeMapping {

    public BukkitAttributeTypeMapping() {
        attributeTypeConverter
                .registerP2W(Attribute.class, entityType -> new AttributeTypeHolder(entityType.name()))
                .registerW2P(Attribute.class, entityTypeHolder -> Attribute.valueOf(entityTypeHolder.getPlatformName()));

        Arrays.stream(Attribute.values()).forEach(attr -> {
            var holder = new AttributeTypeHolder(attr.name());
            mapping.put(AttributeMappingKey.of(attr.name()), holder);
            values.add(holder);
        });
    }
}
