package org.screamingsandals.lib.entity.event;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;

@EqualsAndHashCode(callSuper = false)
public class SItemSpawnEvent extends SEntitySpawnEvent {

    public SItemSpawnEvent(EntityBasic entity) {
        super(entity);
    }
}
