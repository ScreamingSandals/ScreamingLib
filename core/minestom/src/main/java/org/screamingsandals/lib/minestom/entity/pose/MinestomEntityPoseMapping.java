package org.screamingsandals.lib.minestom.entity.pose;

import net.minestom.server.entity.Entity;
import org.screamingsandals.lib.entity.pose.EntityPoseMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;
import java.util.Locale;

@Service
public class MinestomEntityPoseMapping extends EntityPoseMapping {
    public MinestomEntityPoseMapping() {
        entityPoseConverter
                .registerP2W(Entity.Pose.class, MinestomEntityPoseHolder::new)
                .registerW2P(Entity.Pose.class, entityPoseHolder -> Entity.Pose.valueOf(entityPoseHolder.platformName()));

        Arrays.stream(Entity.Pose.values()).forEach(pose -> {
            final var holder = new MinestomEntityPoseHolder(pose);
            mapping.put(NamespacedMappingKey.of(pose.name().toLowerCase(Locale.ROOT)), holder);
            values.add(holder);
        });
    }
}
