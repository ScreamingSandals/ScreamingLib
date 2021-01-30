package org.screamingsandals.lib.player.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.world.LocationHolder;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
public class SPlayerMoveEvent extends CancellableAbstractEvent {
    private final PlayerWrapper player;
    private final LocationHolder currentLocation;
    private LocationHolder newLocation;
}
