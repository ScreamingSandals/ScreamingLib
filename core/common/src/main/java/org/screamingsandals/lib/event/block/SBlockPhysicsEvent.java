package org.screamingsandals.lib.event.block;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.world.BlockHolder;

@EqualsAndHashCode(callSuper = true)
@Data
public class SBlockPhysicsEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<BlockHolder> block;
    private final ImmutableObjectLink<BlockTypeHolder> material;
    private final ImmutableObjectLink<BlockHolder> causingBlock;

    public BlockHolder getBlock() {
        return block.get();
    }

    public BlockTypeHolder getMaterial() {
        return material.get();
    }

    public BlockHolder getCausingBlock() {
        return causingBlock.get();
    }
}
