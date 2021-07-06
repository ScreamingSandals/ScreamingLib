package org.screamingsandals.lib.event.world;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.WorldHolder;

@EqualsAndHashCode(callSuper = true)
@Data
public class SSpawnChangeEvent extends AbstractEvent {
    private final WorldHolder world;
    private final LocationHolder oldLocation;
    private final LocationHolder newLocation;
}
