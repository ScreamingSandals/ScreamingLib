package org.screamingsandals.lib.bukkit.entity.pose;

import org.bukkit.entity.Pose;
import org.screamingsandals.lib.entity.pose.EntityPoseHolder;
import org.screamingsandals.lib.entity.pose.EntityPoseMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.Arrays;

@Service
public class BukkitEntityPoseMapping extends EntityPoseMapping {
    public BukkitEntityPoseMapping() {
        if (Reflect.has("org.bukkit.entity.Pose")) {
            entityPoseConverter
                    .registerP2W(Pose.class, pose -> new EntityPoseHolder(pose.name()))
                    .registerW2P(Pose.class, entityPoseHolder -> Pose.valueOf(entityPoseHolder.getPlatformName()));

            Arrays.stream(Pose.values()).forEach(pose -> {
                var holder = new EntityPoseHolder(pose.name());
                mapping.put(NamespacedMappingKey.of(pose.name()), holder);
                values.add(holder);
            });
        }
    }
}
