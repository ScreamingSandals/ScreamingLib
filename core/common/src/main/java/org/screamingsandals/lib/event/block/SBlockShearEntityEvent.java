package org.screamingsandals.lib.event.block;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.world.BlockHolder;

@EqualsAndHashCode(callSuper = true)
@Data
public class SBlockShearEntityEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<BlockHolder> block;
    private final ImmutableObjectLink<EntityBasic> entity;
    private final ImmutableObjectLink<Item> tool;

    public BlockHolder getBlock() {
        return block.get();
    }

    public EntityBasic getEntity() {
        return entity.get();
    }

    public Item getTool() {
        return tool.get();
    }
}
