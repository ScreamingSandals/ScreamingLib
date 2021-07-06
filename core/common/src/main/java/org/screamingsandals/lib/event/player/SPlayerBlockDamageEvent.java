package org.screamingsandals.lib.event.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.world.BlockHolder;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
public class SPlayerBlockDamageEvent extends CancellableAbstractEvent {
    /**
     * Player who damaged the block
     */
    private final PlayerWrapper player;
    /**
     * Damaged Block
     */
    private final BlockHolder block;
    /**
     * Item which has been used to damage this block
     */
    private final Item itemInHand;
    /**
     * If this damage should instantly break the block or not
     */
    private boolean instantBreak;
}
