package org.screamingsandals.lib.event.world;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.world.WorldHolder;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class SWorldLoadEvent extends AbstractEvent {

    public abstract WorldHolder getWorld();
}
