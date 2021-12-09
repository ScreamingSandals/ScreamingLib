package org.screamingsandals.lib.event.entity;

public interface SEntityPortalEvent extends SEntityTeleportEvent {

    int getSearchRadius();

    void setSearchRadius(int searchRadius);
}
