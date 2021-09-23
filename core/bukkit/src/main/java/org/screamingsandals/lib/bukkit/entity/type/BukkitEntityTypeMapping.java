package org.screamingsandals.lib.bukkit.entity.type;

import org.bukkit.entity.EntityType;
import org.screamingsandals.lib.entity.type.EntityTypeHolder;
import org.screamingsandals.lib.entity.type.EntityTypeMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;

@Service
public class BukkitEntityTypeMapping extends EntityTypeMapping {
    public BukkitEntityTypeMapping() {
        entityTypeConverter
                .registerP2W(EntityType.class, entityType -> new EntityTypeHolder(entityType.name()))
                .registerW2P(EntityType.class, entityTypeHolder -> EntityType.valueOf(entityTypeHolder.getPlatformName()));

        Arrays.stream(EntityType.values()).forEach(entityType -> {
            var holder = new EntityTypeHolder(entityType.name());
            mapping.put(NamespacedMappingKey.of(entityType.name()), holder);
            values.add(holder);
        });
    }

    @Override
    public boolean isAlive0(EntityTypeHolder entityTypeHolder) {
        return entityTypeHolder.as(EntityType.class).isAlive();
    }
}
