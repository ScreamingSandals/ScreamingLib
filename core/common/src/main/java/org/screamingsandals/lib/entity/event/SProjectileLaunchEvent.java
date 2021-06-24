package org.screamingsandals.lib.entity.event;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;

@EqualsAndHashCode(callSuper = true)
public class SProjectileLaunchEvent extends SEntitySpawnEvent {
    public SProjectileLaunchEvent(EntityBasic entity) {
        super(entity);
    }
}
