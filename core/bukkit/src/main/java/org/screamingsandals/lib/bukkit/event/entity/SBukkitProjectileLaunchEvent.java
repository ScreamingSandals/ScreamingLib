package org.screamingsandals.lib.bukkit.event.entity;

import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.screamingsandals.lib.entity.EntityProjectile;
import org.screamingsandals.lib.event.entity.SProjectileLaunchEvent;

public class SBukkitProjectileLaunchEvent extends SBukkitEntitySpawnEvent implements SProjectileLaunchEvent {
    public SBukkitProjectileLaunchEvent(ProjectileLaunchEvent event) {
        super(event);
    }

    @Override
    public EntityProjectile getEntity() {
        return (EntityProjectile) super.getEntity();
    }
}
