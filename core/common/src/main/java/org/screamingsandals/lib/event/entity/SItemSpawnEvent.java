package org.screamingsandals.lib.event.entity;

import org.screamingsandals.lib.entity.EntityItem;

public interface SItemSpawnEvent extends SEntitySpawnEvent {
    @Override
    EntityItem getEntity();
}
