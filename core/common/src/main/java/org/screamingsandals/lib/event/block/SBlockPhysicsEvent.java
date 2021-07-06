package org.screamingsandals.lib.event.block;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.world.BlockHolder;

@EqualsAndHashCode(callSuper = true)
@Data
public class SBlockPhysicsEvent extends CancellableAbstractEvent {
    private final BlockHolder block;
    private final MaterialHolder material;
    private final BlockHolder causingBlock;
}
