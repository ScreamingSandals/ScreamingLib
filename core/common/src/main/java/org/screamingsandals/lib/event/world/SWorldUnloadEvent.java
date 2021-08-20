package org.screamingsandals.lib.event.world;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.world.WorldHolder;

@EqualsAndHashCode(callSuper = true)
@Data
public class SWorldUnloadEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<WorldHolder> world;

    public WorldHolder getWorld() {
        return world.get();
    }
}
