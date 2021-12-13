package org.screamingsandals.lib.event.player;

public interface SPlayerPortalEvent extends SPlayerTeleportEvent {

    int getSearchRadius();

    void setSearchRadius(int getSearchRadius);

    boolean isCanCreatePortal();

    void setCanCreatePortal(boolean canCreatePortal);

    int getCreationRadius();

    void setCreationRadius(int creationRadius);
}
