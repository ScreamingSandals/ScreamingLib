package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.world.BlockHolder;

@EqualsAndHashCode(callSuper = false)
public class SPlayerBlockDamageEvent extends SPlayerCancellableEvent {
    private final ImmutableObjectLink<BlockHolder> block;
    private final ImmutableObjectLink<Item> itemInHand;
    private final ObjectLink<Boolean> instantBreak;

    public SPlayerBlockDamageEvent(ImmutableObjectLink<PlayerWrapper> player,
                                   ImmutableObjectLink<BlockHolder> block,
                                   ImmutableObjectLink<Item> itemInHand,
                                   ObjectLink<Boolean> instantBreak) {
        super(player);
        this.block = block;
        this.itemInHand = itemInHand;
        this.instantBreak = instantBreak;
    }

    /**
     * Damaged Block
     */
    public BlockHolder getBlock() {
        return block.get();
    }

    /**
     * Item which has been used to damage this block
     */
    public Item getItemInHand() {
        return itemInHand.get();
    }

    /**
     * If this damage should instantly break the block or not
     */
    public boolean isInstantBreak() {
        return instantBreak.get();
    }

    public void setInstantBreak(boolean instantBreak) {
        this.instantBreak.set(instantBreak);
    }
}
