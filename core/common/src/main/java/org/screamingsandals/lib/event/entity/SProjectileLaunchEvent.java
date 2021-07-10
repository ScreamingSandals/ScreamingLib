package org.screamingsandals.lib.event.entity;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;

@EqualsAndHashCode(callSuper = true)
public class SProjectileLaunchEvent extends SEntitySpawnEvent {
    public SProjectileLaunchEvent(EntityBasic entity) {
        super(entity);
    }
}
