package org.screamingsandals.lib.event.entity;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.world.BlockHolder;

@EqualsAndHashCode(callSuper = true)
public class SEntityCombustByBlockEvent extends SEntityCombustEvent {
    private final ImmutableObjectLink<BlockHolder> combuster;

    public SEntityCombustByBlockEvent(ImmutableObjectLink<EntityBasic> entity, ObjectLink<Integer> duration, ImmutableObjectLink<BlockHolder> combuster) {
        super(entity, duration);
        this.combuster = combuster;
    }

    public BlockHolder getCombuster() {
        return combuster.get();
    }
}
