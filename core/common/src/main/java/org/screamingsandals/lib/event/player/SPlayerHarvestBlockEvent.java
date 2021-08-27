package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.world.BlockHolder;

import java.util.Collection;

@EqualsAndHashCode(callSuper = false)
public class SPlayerHarvestBlockEvent extends SPlayerCancellableEvent {
    private final ImmutableObjectLink<BlockHolder> harvestedBlock;
    private final Collection<Item> itemsHarvested;

    public SPlayerHarvestBlockEvent(ImmutableObjectLink<PlayerWrapper> player,
                                    ImmutableObjectLink<BlockHolder> harvestedBlock,
                                    Collection<Item> itemsHarvested) {
        super(player);
        this.harvestedBlock = harvestedBlock;
        this.itemsHarvested = itemsHarvested;
    }

    public BlockHolder getHarvestedBlock() {
        return harvestedBlock.get();
    }
}
