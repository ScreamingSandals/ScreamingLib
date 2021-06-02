package org.screamingsandals.lib.world.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.world.BlockHolder;
import org.screamingsandals.lib.world.state.BlockStateHolder;

@EqualsAndHashCode(callSuper = true)
@Data
public class SBlockGrowEvent extends CancellableAbstractEvent {
    private final BlockHolder block;
    private final BlockStateHolder newBlockState;
}
