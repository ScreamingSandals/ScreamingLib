package org.screamingsandals.lib.event.entity;

import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SEvent;
import org.screamingsandals.lib.world.LocationHolder;

public interface SEntityPortalEnterEvent extends SEvent, PlatformEventWrapper {

    EntityBasic getEntity();

    LocationHolder getLocation();
}
