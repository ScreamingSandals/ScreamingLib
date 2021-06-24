package org.screamingsandals.lib.minestom.entity.type;

import net.minestom.server.entity.EntityType;
import org.screamingsandals.lib.entity.type.EntityTypeHolder;
import org.screamingsandals.lib.entity.type.EntityTypeMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;

@Service
public class MinestomEntityTypeMapping extends EntityTypeMapping {
    public static void init() {
        EntityTypeMapping.init(MinestomEntityTypeMapping::new);
    }

    public MinestomEntityTypeMapping() {
        entityTypeConverter
                .registerP2W(EntityType.class, entityType -> new EntityTypeHolder(entityType.name()))
                .registerW2P(EntityType.class, entityTypeHolder -> EntityType.valueOf(entityTypeHolder.getPlatformName()));

        Arrays.stream(EntityType.values())
                .forEach(entityType -> mapping.put(NamespacedMappingKey.of(entityType.getNamespaceID()), new EntityTypeHolder(entityType.name())));
    }
}
