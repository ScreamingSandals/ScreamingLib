package org.screamingsandals.lib.bukkit.event.entity;

import org.bukkit.event.entity.EntityPortalEvent;
import org.screamingsandals.lib.event.entity.SEntityPortalEvent;

public class SBukkitEntityPortalEvent extends SBukkitEntityTeleportEvent implements SEntityPortalEvent {
    public SBukkitEntityPortalEvent(EntityPortalEvent event) {
        super(event);
    }

    @Override
    public EntityPortalEvent getEvent() {
        return (EntityPortalEvent) super.getEvent();
    }

    @Override
    public int getSearchRadius() {
        return getEvent().getSearchRadius();
    }

    @Override
    public void setSearchRadius(int searchRadius) {
        getEvent().setSearchRadius(searchRadius);
    }
}
