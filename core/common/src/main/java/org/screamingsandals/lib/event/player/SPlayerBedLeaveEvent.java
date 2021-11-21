package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.block.BlockHolder;

public interface SPlayerBedLeaveEvent extends SCancellableEvent, SPlayerEvent {

    BlockHolder getBed();

    boolean isBedSpawn();

    void setBedSpawn(boolean bedSpawn);
}
