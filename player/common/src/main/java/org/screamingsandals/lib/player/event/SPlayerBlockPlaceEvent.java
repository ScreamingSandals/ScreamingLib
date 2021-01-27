package org.screamingsandals.lib.player.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.world.BlockHolder;

@EqualsAndHashCode(callSuper = false)
@Data
public class SPlayerBlockPlaceEvent extends CancellableAbstractEvent {
    private final PlayerWrapper player;
    private final BlockHolder block;

}
