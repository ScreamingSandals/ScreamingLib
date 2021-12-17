package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.event.PlatformEventWrapper;

public interface SPlayerPortalEvent extends SPlayerTeleportEvent, PlatformEventWrapper {

    int getSearchRadius();

    void setSearchRadius(int getSearchRadius);

    boolean isCanCreatePortal();

    void setCanCreatePortal(boolean canCreatePortal);

    int getCreationRadius();

    void setCreationRadius(int creationRadius);
}
