package org.screamingsandals.lib.event.world;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.annotations.LimitedVersionSupport;
import org.screamingsandals.lib.world.BlockHolder;
import org.screamingsandals.lib.world.state.BlockStateHolder;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
@LimitedVersionSupport("Bukkit >= 1.13")
public class SSpongeAbsorbEvent extends CancellableAbstractEvent {
    public final BlockHolder block;
    public final Collection<BlockStateHolder> waterBlocks;
}
