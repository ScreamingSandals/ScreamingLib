package org.screamingsandals.lib.event.world;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.block.state.BlockStateHolder;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class SPlantGrowEvent extends CancellableAbstractEvent {

    public abstract Collection<BlockStateHolder> getBlockStates();

    public abstract LocationHolder getLocation();

    @Nullable
    public abstract PlayerWrapper getPlayer();

    public abstract boolean isBonemealed();
}
