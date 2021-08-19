package org.screamingsandals.lib.event.block;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.world.BlockHolder;

@EqualsAndHashCode(callSuper = true)
@Data
public class SBlockPhysicsEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<BlockHolder> block;
    private final ImmutableObjectLink<MaterialHolder> material;
    private final ImmutableObjectLink<BlockHolder> causingBlock;

    public BlockHolder getBlock() {
        return block.get();
    }

    public MaterialHolder getMaterial() {
        return material.get();
    }

    public BlockHolder getCausingBlock() {
        return causingBlock.get();
    }
}
