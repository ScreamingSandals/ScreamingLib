package org.screamingsandals.lib.bukkit.entity.type;

import org.bukkit.entity.EntityType;
import org.screamingsandals.lib.entity.type.EntityTypeMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;

@Service
public class BukkitEntityTypeMapping extends EntityTypeMapping {
    public BukkitEntityTypeMapping() {
        entityTypeConverter
                .registerP2W(EntityType.class, BukkitEntityTypeHolder::new);

        Arrays.stream(EntityType.values()).forEach(entityType -> {
            var holder = new BukkitEntityTypeHolder(entityType);
            mapping.put(NamespacedMappingKey.of(entityType.name()), holder);
            values.add(holder);
        });
    }
}
