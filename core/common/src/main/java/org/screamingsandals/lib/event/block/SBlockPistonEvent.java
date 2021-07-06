package org.screamingsandals.lib.event.block;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.BlockFace;
import org.screamingsandals.lib.world.BlockHolder;

@EqualsAndHashCode(callSuper = true)
@Data
public class SBlockPistonEvent extends CancellableAbstractEvent {
    private final BlockHolder block;
    private final boolean sticky;
    private final BlockFace direction;
}
