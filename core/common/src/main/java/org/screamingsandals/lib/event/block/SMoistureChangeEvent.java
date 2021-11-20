package org.screamingsandals.lib.event.block;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.state.BlockStateHolder;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class SMoistureChangeEvent extends CancellableAbstractEvent {

    public abstract BlockHolder getBlock();

    public abstract BlockStateHolder getNewBlockState();
}
