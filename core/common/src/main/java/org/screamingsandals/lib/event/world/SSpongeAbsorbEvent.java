package org.screamingsandals.lib.event.world;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;
import org.screamingsandals.lib.world.BlockHolder;
import org.screamingsandals.lib.world.state.BlockStateHolder;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
@LimitedVersionSupport("Bukkit >= 1.13")
public class SSpongeAbsorbEvent extends CancellableAbstractEvent {
    public final ImmutableObjectLink<BlockHolder> block;
    public final Collection<BlockStateHolder> waterBlocks;

    public BlockHolder getBlock() {
        return block.get();
    }
}
