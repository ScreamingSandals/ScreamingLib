package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.event.SEvent;
import org.screamingsandals.lib.world.LocationHolder;

public interface SPlayerRespawnEvent extends SEvent, SPlayerEvent {

    LocationHolder getLocation();

    void setLocation(LocationHolder location);
}
