package org.screamingsandals.lib.event.world;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.WorldHolder;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class SSpawnChangeEvent extends AbstractEvent {

    public abstract WorldHolder getWorld();

    public abstract LocationHolder getOldLocation();

    public abstract LocationHolder getNewLocation();
}
