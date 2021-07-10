package org.screamingsandals.lib.event.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.world.LocationHolder;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
public class SPlayerRespawnEvent extends CancellableAbstractEvent {
    private final PlayerWrapper player;
    private LocationHolder location;
}
