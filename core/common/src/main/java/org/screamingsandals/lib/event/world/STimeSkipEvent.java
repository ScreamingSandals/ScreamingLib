package org.screamingsandals.lib.event.world;

import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;
import org.screamingsandals.lib.world.WorldHolder;

@LimitedVersionSupport("Bukkit >= 1.15.1")
public interface STimeSkipEvent extends SCancellableEvent {

    WorldHolder getWorld();

    Reason getReason();

    long getSkipAmount();

    void setSkipAmount(long skipAmount);

    // TODO: holder?
    enum Reason {
        COMMAND,
        CUSTOM,
        NIGHT_SKIP
    }
}
