package org.screamingsandals.lib.event.block;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.world.BlockHolder;
import org.screamingsandals.lib.world.state.BlockStateHolder;

@EqualsAndHashCode(callSuper = true)
@Getter
public class SBlockFormedByEntityEvent extends SBlockFormEvent {
    private final ImmutableObjectLink<EntityBasic> producer;

    public SBlockFormedByEntityEvent(ImmutableObjectLink<BlockHolder> block, ImmutableObjectLink<EntityBasic> producer, ImmutableObjectLink<BlockStateHolder> newBlockState) {
        super(block, newBlockState);
        this.producer = producer;
    }

    public EntityBasic getProducer() {
        return producer.get();
    }
}
