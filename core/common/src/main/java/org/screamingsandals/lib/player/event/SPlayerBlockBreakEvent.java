package org.screamingsandals.lib.player.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.world.BlockHolder;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
public class SPlayerBlockBreakEvent extends CancellableAbstractEvent {
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
}
