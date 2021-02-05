package org.screamingsandals.lib.player.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.player.PlayerWrapper;

@EqualsAndHashCode(callSuper = false)
@Data
public class SPlayerPickupItemEvent extends CancellableAbstractEvent {
    private final PlayerWrapper player;
    private final Item item;
}
