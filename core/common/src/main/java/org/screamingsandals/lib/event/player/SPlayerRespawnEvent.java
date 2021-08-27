package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.world.LocationHolder;

@EqualsAndHashCode(callSuper = false)
public class SPlayerRespawnEvent extends SPlayerCancellableEvent {
    private final ObjectLink<LocationHolder> location;

    public SPlayerRespawnEvent(ImmutableObjectLink<PlayerWrapper> player,
                               ObjectLink<LocationHolder> location) {
        super(player);
        this.location = location;
    }

    public LocationHolder getLocation() {
        return location.get();
    }

    public void setLocation(LocationHolder location) {
        this.location.set(location);
    }
}
