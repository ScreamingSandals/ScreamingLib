package org.screamingsandals.lib.event.player;

import lombok.*;
import org.screamingsandals.lib.event.Cancellable;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.world.BlockHolder;
import org.screamingsandals.lib.event.block.SBlockExperienceEvent;

@EqualsAndHashCode(callSuper = false)
@ToString
@Getter
@Setter
public class SPlayerBlockBreakEvent extends SBlockExperienceEvent implements Cancellable {
    /**
     * Player who placed the block
     */
    private final PlayerWrapper player;
    /**
     * Placed block
     */
    private final BlockHolder block;
    /**
     * If this event should drop any item on the group
     */
    private boolean dropItems;
    private boolean cancelled;

    public SPlayerBlockBreakEvent(PlayerWrapper player, BlockHolder block, boolean dropItems, int experience) {
        super(block, experience);
        this.player = player;
        this.block = block;
        this.dropItems = dropItems;
    }
}
