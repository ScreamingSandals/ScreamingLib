package org.screamingsandals.lib.event.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.world.LocationHolder;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
public class SPlayerMoveEvent extends CancellableAbstractEvent implements SPlayerEvent {
    private final ImmutableObjectLink<PlayerWrapper> player;
    private final ObjectLink<LocationHolder> currentLocation; // mutable in bukkit
    private final ObjectLink<LocationHolder> newLocation;

    public PlayerWrapper getPlayer() {
        return player.get();
    }

    public LocationHolder getCurrentLocation() {
        return currentLocation.get();
    }

    public LocationHolder getNewLocation() {
        return newLocation.get();
    }

    public void setNewLocation(LocationHolder newLocation) {
        this.newLocation.set(newLocation);
    }
}
