package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.state.BlockStateHolder;

import java.util.Collection;

public interface SPlayerBlockPlaceEvent extends SCancellableEvent, SPlayerEvent, PlatformEventWrapper {
    /**
     * Other blocks that were also placed by the placement of block. They may have material type of AIR. This list also includes the main block
     */
    Collection<BlockStateHolder> getReplacedBlockStates();

    /**
     * Hand used to place this block
     */
    PlayerWrapper.Hand getPlayerHand();

    /**
     * Placed block
     */
    BlockHolder getBlock();

    /**
     * Replaced block state
     */
    BlockStateHolder getReplacedBlockState();

    /**
     * Item in hand
     */
    Item getItemInHand();
}
