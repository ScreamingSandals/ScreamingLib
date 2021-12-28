package org.screamingsandals.lib.minestom.entity.type;

import net.minestom.server.entity.EntitySpawnType;
import net.minestom.server.entity.EntityType;
import org.screamingsandals.lib.entity.type.EntityTypeHolder;
import org.screamingsandals.lib.entity.type.EntityTypeMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

@Service
public class MinestomEntityTypeMapping extends EntityTypeMapping {
    public MinestomEntityTypeMapping() {
        entityTypeConverter
                .registerP2W(EntityType.class, entityType -> new EntityTypeHolder(entityType.name()))
                .registerW2P(EntityType.class, entityTypeHolder -> EntityType.fromNamespaceId(entityTypeHolder.getPlatformName()));

        EntityType.values().forEach(entityType -> {
            final var holder = new EntityTypeHolder(entityType.name());
            mapping.put(NamespacedMappingKey.of(entityType.name()), holder);
            values.add(holder);
        });
    }

    @Override
    public boolean isAlive0(EntityTypeHolder entityTypeHolder) {
        final var spawnType = entityTypeHolder.as(EntityType.class).registry().spawnType();
        return spawnType == EntitySpawnType.LIVING || spawnType == EntitySpawnType.PLAYER;
    }
}
