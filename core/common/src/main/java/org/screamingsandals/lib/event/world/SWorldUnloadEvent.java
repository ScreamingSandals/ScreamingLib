package org.screamingsandals.lib.event.world;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.world.WorldHolder;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class SWorldUnloadEvent extends CancellableAbstractEvent {

    public abstract WorldHolder getWorld();
}
