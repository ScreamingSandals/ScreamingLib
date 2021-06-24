package org.screamingsandals.lib.world.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.BlockFace;
import org.screamingsandals.lib.world.BlockHolder;

@EqualsAndHashCode(callSuper = true)
@Data
public class SBlockFormToEvent extends CancellableAbstractEvent {
    private final BlockHolder sourceBlock;
    private final BlockHolder facedBlock;
    private final BlockFace face;
}
