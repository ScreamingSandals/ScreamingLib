package org.screamingsandals.lib.event.entity;

import org.screamingsandals.lib.entity.EntityProjectile;

public interface SProjectileLaunchEvent extends SEntitySpawnEvent {

    @Override
    EntityProjectile getEntity();
}
