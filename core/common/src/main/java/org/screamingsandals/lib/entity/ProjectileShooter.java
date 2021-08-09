package org.screamingsandals.lib.entity;

import org.screamingsandals.lib.entity.type.EntityTypeHolder;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.math.Vector3D;

import java.util.Optional;

public interface ProjectileShooter extends Wrapper {
    default Optional<EntityProjectile> launchProjectile(Object projectileType) {
        if (projectileType instanceof EntityTypeHolder) {
            return launchProjectile(projectileType);
        } else {
            return EntityTypeHolder.ofOptional(projectileType).flatMap(this::launchProjectile);
        }
    }

    Optional<EntityProjectile> launchProjectile(EntityTypeHolder projectileType);

    default Optional<EntityProjectile> launchProjectile(Object projectileType, Vector3D velocity) {
        if (projectileType instanceof EntityTypeHolder) {
            return launchProjectile(projectileType, velocity);
        } else {
            return EntityTypeHolder.ofOptional(projectileType).flatMap(entityTypeHolder -> launchProjectile(entityTypeHolder, velocity));
        }
    }

    Optional<EntityProjectile> launchProjectile(EntityTypeHolder projectileType, Vector3D velocity);
}
