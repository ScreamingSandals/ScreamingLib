package org.screamingsandals.lib.event.block;

import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SEvent;
import org.screamingsandals.lib.block.BlockHolder;

public interface SBlockExperienceEvent extends SEvent, PlatformEventWrapper {

    BlockHolder getBlock();

    int getExperience();

    void setExperience(int experience);
}
