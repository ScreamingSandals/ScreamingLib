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
public class SPlayerRespawnEvent extends CancellableAbstractEvent implements SPlayerEvent  {
    private final ImmutableObjectLink<PlayerWrapper> player;
    private final ObjectLink<LocationHolder> location;

    public PlayerWrapper getPlayer() {
        return player.get();
    }

    public LocationHolder getLocation() {
        return location.get();
    }

    public void setLocation(LocationHolder location) {
        this.location.set(location);
    }
}
