package org.screamingsandals.lib.event.world;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.WorldHolder;

@EqualsAndHashCode(callSuper = true)
@Data
public class SSpawnChangeEvent extends AbstractEvent {
    private final ImmutableObjectLink<WorldHolder> world;
    private final ImmutableObjectLink<LocationHolder> oldLocation;
    private final ImmutableObjectLink<LocationHolder> newLocation;

    public WorldHolder getWorld() {
        return world.get();
    }

    public LocationHolder getOldLocation() {
        return oldLocation.get();
    }

    public LocationHolder getNewLocation() {
        return newLocation.get();
    }
}
