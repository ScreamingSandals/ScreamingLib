package org.screamingsandals.lib.event.entity;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

@EqualsAndHashCode(callSuper = false)
public class SItemSpawnEvent extends SEntitySpawnEvent {

    public SItemSpawnEvent(ImmutableObjectLink<EntityBasic> entity) {
        super(entity);
    }
}
