package org.screamingsandals.lib.event.world;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.world.WorldHolder;

@EqualsAndHashCode(callSuper = true)
@Data
public class SWorldSaveEvent extends AbstractEvent {
    private final ImmutableObjectLink<WorldHolder> world;

    public WorldHolder getWorld() {
        return world.get();
    }
}
