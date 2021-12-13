package org.screamingsandals.lib.bukkit.event.player;

import org.bukkit.event.player.PlayerPortalEvent;
import org.screamingsandals.lib.event.player.SPlayerPortalEvent;

public class SBukkitPlayerPortalEvent extends SBukkitPlayerTeleportEvent implements SPlayerPortalEvent {
    public SBukkitPlayerPortalEvent(PlayerPortalEvent event) {
        super(event);
    }

    @Override
    public PlayerPortalEvent getEvent() {
        return (PlayerPortalEvent) super.getEvent();
    }

    @Override
    public int getSearchRadius() {
        return getEvent().getSearchRadius();
    }

    @Override
    public void setSearchRadius(int getSearchRadius) {
        getEvent().setSearchRadius(getSearchRadius);
    }

    @Override
    public boolean isCanCreatePortal() {
        return getEvent().getCanCreatePortal();
    }

    @Override
    public void setCanCreatePortal(boolean canCreatePortal) {
        getEvent().setCanCreatePortal(canCreatePortal);
    }

    @Override
    public int getCreationRadius() {
        return getEvent().getCreationRadius();
    }

    @Override
    public void setCreationRadius(int creationRadius) {
        getEvent().setCreationRadius(creationRadius);
    }
}
