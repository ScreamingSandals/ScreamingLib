package org.screamingsandals.lib.event.block;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.event.world.SPlantGrowEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;
import org.screamingsandals.lib.world.BlockHolder;
import org.screamingsandals.lib.world.state.BlockStateHolder;

import java.util.Collection;

/**
 * @see SPlantGrowEvent
 */
@EqualsAndHashCode(callSuper = true)
@Data
@LimitedVersionSupport("Bukkit >= 1.13")
public class SBlockFertilizeEvent extends CancellableAbstractEvent {
    private final PlayerWrapper player;
    private final BlockHolder block;
    private final Collection<BlockStateHolder> changedBlockStates;
}
