package org.screamingsandals.lib.event.player;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.world.WorldHolder;

@EqualsAndHashCode(callSuper = false)
@Data
public class SPlayerWorldChangeEvent extends CancellableAbstractEvent {
    private final PlayerWrapper player;
    private final WorldHolder from;
}
