package org.screamingsandals.lib.world.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.world.BlockHolder;
import org.screamingsandals.lib.world.state.BlockStateHolder;

@EqualsAndHashCode(callSuper = true)
@Getter
public class SBlockFormedByEntityEvent extends SBlockFormEvent {
    private final Producer producer;

    public SBlockFormedByEntityEvent(BlockHolder block, Producer producer, BlockStateHolder newBlockState) {
        super(block, newBlockState);
        this.producer = producer;
    }

    public interface Producer extends Wrapper {
    }
}
