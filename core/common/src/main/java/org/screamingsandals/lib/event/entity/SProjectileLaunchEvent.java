package org.screamingsandals.lib.event.entity;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

@EqualsAndHashCode(callSuper = true)
public class SProjectileLaunchEvent extends SEntitySpawnEvent {
    public SProjectileLaunchEvent(ImmutableObjectLink<EntityBasic> entity) {
        super(entity);
    }
}
