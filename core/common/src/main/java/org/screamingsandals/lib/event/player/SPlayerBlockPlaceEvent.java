package org.screamingsandals.lib.event.player;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.state.BlockStateHolder;

import java.util.Collection;

@EqualsAndHashCode(callSuper = false)
@Data
public class SPlayerBlockPlaceEvent extends CancellableAbstractEvent implements SPlayerEvent {
    private final ImmutableObjectLink<PlayerWrapper> player;
    private final ImmutableObjectLink<PlayerWrapper.Hand> playerHand;
    private final ImmutableObjectLink<BlockHolder> block;
    private final ImmutableObjectLink<BlockStateHolder> replacedBlockState;
    private final ImmutableObjectLink<Item> itemInHand;
    /**
     * Other blocks that were also placed by the placement of block. They may have material type of AIR. This list also includes the main block
     */
    private final Collection<BlockStateHolder> replacedBlockStates;

    /**
     * Player who placed the block
     */
    public PlayerWrapper getPlayer() {
        return player.get();
    }

    /**
     * Hand used to place this block
     */
    public PlayerWrapper.Hand getPlayerHand() {
        return playerHand.get();
    }

    /**
     * Placed block
     */
    public BlockHolder getBlock() {
        return block.get();
    }

    /**
     * Replaced block state
     */
    public BlockStateHolder getReplacedBlockState() {
        return replacedBlockState.get();
    }

    /**
     * Item in hand
     */
    public Item getItemInHand() {
        return itemInHand.get();
    }
}
