package org.screamingsandals.lib.minestom.entity.type;

import net.minestom.server.entity.EntityType;
import org.screamingsandals.lib.entity.type.EntityTypeMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

@Service
public class MinestomEntityTypeMapping extends EntityTypeMapping {
    public MinestomEntityTypeMapping() {
        entityTypeConverter
                .registerP2W(EntityType.class, MinestomEntityTypeHolder::new)
                .registerW2P(EntityType.class, entityTypeHolder -> EntityType.fromNamespaceId(entityTypeHolder.platformName()));

        EntityType.values().forEach(entityType -> {
            final var holder = new MinestomEntityTypeHolder(entityType);
            mapping.put(NamespacedMappingKey.of(entityType.name()), holder);
            values.add(holder);
        });
    }
}
