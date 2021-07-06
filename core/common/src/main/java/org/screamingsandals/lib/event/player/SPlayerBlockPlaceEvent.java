package org.screamingsandals.lib.event.player;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.world.BlockHolder;
import org.screamingsandals.lib.world.state.BlockStateHolder;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class SPlayerBlockPlaceEvent extends CancellableAbstractEvent {
    /**
     * Player who placed the block
     */
    private final PlayerWrapper player;
    /**
     * Hand used to place this block
     */
    private final PlayerWrapper.Hand playerHand;
    /**
     * Placed block
     */
    private final BlockHolder block;
    /**
     * Replaced block state
     */
    private final BlockStateHolder replacedBlockState;
    /**
     * Item in hand
     */
    private final Item itemInHand;
    /**
     * Hand used to place this block
     */
    private final PlayerWrapper.Hand hand;
    /**
     * Other blocks that were also placed by the placement of block. They may have material type of AIR. This list also includes the main block
     */
    private final List<BlockStateHolder> replacedBlockStates;
}
