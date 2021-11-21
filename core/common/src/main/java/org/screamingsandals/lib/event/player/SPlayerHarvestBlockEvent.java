package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.item.Item;

import java.util.Collection;

public interface SPlayerHarvestBlockEvent extends SCancellableEvent, SPlayerEvent {

    Collection<Item> getItemsHarvested();

    BlockHolder getHarvestedBlock();
}
