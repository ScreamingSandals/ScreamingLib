package org.screamingsandals.lib.event.entity;

import org.screamingsandals.lib.entity.EntityProjectile;
import org.screamingsandals.lib.entity.ProjectileShooter;

public interface SProjectileLaunchEvent extends SEntitySpawnEvent {

    @Override
    EntityProjectile getEntity();

    ProjectileShooter getShooter();
}
