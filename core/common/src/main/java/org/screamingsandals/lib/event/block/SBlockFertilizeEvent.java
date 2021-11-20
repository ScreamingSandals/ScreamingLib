package org.screamingsandals.lib.event.block;

import org.screamingsandals.lib.block.state.BlockStateHolder;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.event.world.SPlantGrowEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;
import org.screamingsandals.lib.block.BlockHolder;

import java.util.Collection;

/**
 * @see SPlantGrowEvent
 */

@LimitedVersionSupport("Bukkit >= 1.13")
public interface SBlockFertilizeEvent extends SCancellableEvent {

    PlayerWrapper getPlayer();

    BlockHolder getBlock();

    Collection<BlockStateHolder> getChangedBlockStates();
}
