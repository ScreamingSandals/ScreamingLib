package org.screamingsandals.lib.event.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.block.BlockHolder;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SEntityEnterBlockEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<EntityBasic> entity;
    private final ImmutableObjectLink<BlockHolder> block;

    public EntityBasic getEntity() {
        return entity.get();
    }

    public BlockHolder getBlock() {
        return block.get();
    }
}
