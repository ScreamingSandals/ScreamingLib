package org.screamingsandals.lib.event.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.block.BlockHolder;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
public class SPlayerBlockDamageEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<PlayerWrapper> player;
    private final ImmutableObjectLink<BlockHolder> block;
    private final ImmutableObjectLink<Item> itemInHand;
    private final ObjectLink<Boolean> instantBreak;

    /**
     * Player who damaged the block
     */
    public PlayerWrapper getPlayer() {
        return player.get();
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
