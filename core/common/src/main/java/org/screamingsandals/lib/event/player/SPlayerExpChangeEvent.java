package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.event.PlatformEventWrapper;

public interface SPlayerExpChangeEvent extends SPlayerEvent, PlatformEventWrapper {

    int getExp();

    void setExp(int exp);
}
