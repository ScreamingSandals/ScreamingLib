package org.screamingsandals.lib.entity;

import org.screamingsandals.lib.world.BlockHolder;

public interface BlockProjectileShooter extends ProjectileShooter {
    BlockHolder getBlock();
}
