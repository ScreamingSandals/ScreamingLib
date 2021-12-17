package org.screamingsandals.lib.bukkit.event.entity;

import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.EntityProjectile;
import org.screamingsandals.lib.entity.ProjectileShooter;
import org.screamingsandals.lib.event.entity.SProjectileLaunchEvent;

public class SBukkitProjectileLaunchEvent extends SBukkitEntitySpawnEvent implements SProjectileLaunchEvent {
    public SBukkitProjectileLaunchEvent(ProjectileLaunchEvent event) {
        super(event);
    }

    // Internal cache
    private ProjectileShooter shooter;

    @Override
    public EntityProjectile getEntity() {
        return (EntityProjectile) super.getEntity();
    }

    @Override
    @Nullable
    public ProjectileShooter getShooter() {
        if (shooter == null) {
            shooter = getEntity().getShooter();
        }
        return shooter;
    }
}
