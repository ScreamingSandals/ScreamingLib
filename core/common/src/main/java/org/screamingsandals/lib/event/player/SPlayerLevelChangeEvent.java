package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.event.PlatformEventWrapper;

public interface SPlayerLevelChangeEvent extends SPlayerEvent, PlatformEventWrapper {

    int getOldLevel();

    int getNewLevel();
}
