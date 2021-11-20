package org.screamingsandals.lib.event.world;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.state.BlockStateHolder;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
@LimitedVersionSupport("Bukkit >= 1.13")
public abstract class SSpongeAbsorbEvent extends CancellableAbstractEvent {

    public abstract BlockHolder getBlock();

    public abstract Collection<BlockStateHolder> getWaterBlocks();
}
