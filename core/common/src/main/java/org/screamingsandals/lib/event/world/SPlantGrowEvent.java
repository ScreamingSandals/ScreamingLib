package org.screamingsandals.lib.event.world;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.block.state.BlockStateHolder;

import java.util.Collection;

public interface SPlantGrowEvent extends SCancellableEvent, PlatformEventWrapper {

    Collection<BlockStateHolder> getBlockStates();

    LocationHolder getLocation();

    @Nullable
    PlayerWrapper getPlayer();

    boolean isBonemealed();
}
