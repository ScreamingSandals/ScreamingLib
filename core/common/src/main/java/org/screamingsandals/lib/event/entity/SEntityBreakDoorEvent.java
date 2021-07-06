package org.screamingsandals.lib.event.entity;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.world.BlockDataHolder;
import org.screamingsandals.lib.world.BlockHolder;

@EqualsAndHashCode(callSuper = true)
public class SEntityBreakDoorEvent extends SEntityChangeBlockEvent {

    public SEntityBreakDoorEvent(EntityBasic entity, BlockHolder block, BlockDataHolder to) {
        super(entity, block, to);
    }

}
