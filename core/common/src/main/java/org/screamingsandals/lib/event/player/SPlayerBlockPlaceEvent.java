package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.world.BlockHolder;
import org.screamingsandals.lib.world.state.BlockStateHolder;

import java.util.Collection;

@EqualsAndHashCode(callSuper = false)
public class SPlayerBlockPlaceEvent extends SPlayerCancellableEvent {
    private final ImmutableObjectLink<PlayerWrapper.Hand> playerHand;
    private final ImmutableObjectLink<BlockHolder> block;
    private final ImmutableObjectLink<BlockStateHolder> replacedBlockState;
    private final ImmutableObjectLink<Item> itemInHand;
    /**
     * Other blocks that were also placed by the placement of block. They may have material type of AIR. This list also includes the main block
     */
    private final Collection<BlockStateHolder> replacedBlockStates;

    public SPlayerBlockPlaceEvent(ImmutableObjectLink<PlayerWrapper> player,
                                 ImmutableObjectLink<PlayerWrapper.Hand> playerHand,
                                 ImmutableObjectLink<BlockHolder> block,
                                 ImmutableObjectLink<BlockStateHolder> replacedBlockState,
                                 ImmutableObjectLink<Item> itemInHand,
                                  Collection<BlockStateHolder> replacedBlockStates) {
        super(player);
        this.playerHand = playerHand;
        this.block = block;
        this.replacedBlockState = replacedBlockState;
        this.itemInHand = itemInHand;
        this.replacedBlockStates = replacedBlockStates;
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
