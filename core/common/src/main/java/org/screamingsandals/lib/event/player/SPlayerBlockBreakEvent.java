package org.screamingsandals.lib.event.player;

import lombok.*;
import org.screamingsandals.lib.event.Cancellable;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.event.block.SBlockExperienceEvent;

@EqualsAndHashCode(callSuper = false)
@ToString
@Getter
@Setter
public class SPlayerBlockBreakEvent extends SBlockExperienceEvent implements Cancellable, SPlayerEvent{
    /**
     * Player who placed the block
     */
    private final ImmutableObjectLink<PlayerWrapper> player;
    /**
     * Placed block
     */
    private final ImmutableObjectLink<BlockHolder> block;
    /**
     * If this event should drop any item on the group
     */
    private final ObjectLink<Boolean> dropItems;
    private boolean cancelled;

    public SPlayerBlockBreakEvent(ImmutableObjectLink<PlayerWrapper> player, ImmutableObjectLink<BlockHolder> block, ObjectLink<Boolean> dropItems, ObjectLink<Integer> experience) {
        super(block, experience);
        this.player = player;
        this.block = block;
        this.dropItems = dropItems;
    }

    public PlayerWrapper getPlayer() {
        return player.get();
    }

    public BlockHolder getBlock() {
        return block.get();
    }

    public boolean isDropItems() {
        return dropItems.get();
    }

    public void setDropItems(boolean dropItems) {
        this.dropItems.set(dropItems);
    }
}
