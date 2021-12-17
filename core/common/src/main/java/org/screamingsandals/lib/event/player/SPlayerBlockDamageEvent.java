package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.block.BlockHolder;

public interface SPlayerBlockDamageEvent extends SCancellableEvent, SPlayerEvent, PlatformEventWrapper {

    /**
     * Damaged Block
     */
    BlockHolder getBlock();

    /**
     * Item which has been used to damage this block
     */
    Item getItemInHand();

    /**
     * If this damage should instantly break the block or not
     */
    boolean isInstantBreak();

    void setInstantBreak(boolean instantBreak);
}
