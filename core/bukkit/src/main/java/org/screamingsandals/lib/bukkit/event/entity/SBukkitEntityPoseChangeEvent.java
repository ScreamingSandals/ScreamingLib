package org.screamingsandals.lib.bukkit.event.entity;

import lombok.Data;
import org.bukkit.event.entity.EntityPoseChangeEvent;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.pose.EntityPoseHolder;
import org.screamingsandals.lib.event.entity.SEntityPoseChangeEvent;

@Data
public class SBukkitEntityPoseChangeEvent implements SEntityPoseChangeEvent {
    private final EntityPoseChangeEvent event;

    // Internal cache
    private EntityBasic entity;
    private EntityPoseHolder pose;

    @Override
    public EntityBasic getEntity() {
        if (entity == null) {
            entity = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    public EntityPoseHolder getPose() {
        if (pose == null) {
            pose = EntityPoseHolder.of(event.getPose());
        }
        return pose;
    }
}
