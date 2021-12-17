package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.world.WorldHolder;

public interface SPlayerWorldChangeEvent extends SPlayerEvent, PlatformEventWrapper {

    WorldHolder getFrom();
}
