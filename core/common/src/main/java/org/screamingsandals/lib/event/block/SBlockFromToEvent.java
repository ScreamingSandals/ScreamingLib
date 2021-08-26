package org.screamingsandals.lib.event.block;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.BlockFace;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.block.BlockHolder;

@EqualsAndHashCode(callSuper = true)
@Data
public class SBlockFromToEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<BlockHolder> sourceBlock;
    private final ImmutableObjectLink<BlockHolder> facedBlock;
    private final ImmutableObjectLink<BlockFace> face;

    public BlockHolder getSourceBlock() {
        return sourceBlock.get();
    }

    public BlockHolder getFacedBlock() {
        return facedBlock.get();
    }

    public BlockFace getFace() {
        return face.get();
    }
}
