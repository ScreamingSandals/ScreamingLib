package org.screamingsandals.lib.event.world;

import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SEvent;
import org.screamingsandals.lib.world.WorldHolder;

public interface SWorldSaveEvent extends SEvent, PlatformEventWrapper {
    WorldHolder getWorld();
}
