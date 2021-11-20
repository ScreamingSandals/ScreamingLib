package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.event.Cancellable;
import org.screamingsandals.lib.event.block.SBlockExperienceEvent;

public interface SPlayerBlockBreakEvent extends SBlockExperienceEvent, SPlayerEvent, Cancellable {

    boolean isDropItems();

    void setDropItems(boolean dropItems);
}
