package org.screamingsandals.lib.event.world;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.state.BlockStateHolder;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
public class SPlantGrowEvent extends CancellableAbstractEvent {
    private final Collection<BlockStateHolder> blockStates;
    private final LocationHolder location;
    @Nullable
    private final PlayerWrapper player;
    private final boolean bonemealed;
    // TODO: Tree type
}
