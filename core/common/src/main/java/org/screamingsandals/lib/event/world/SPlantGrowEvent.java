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
public class SPlantGrowEvent extends CancellableAbstractEvent {
    private final Collection<BlockStateHolder> blockStates;
    private final ImmutableObjectLink<LocationHolder> location;
    private final ImmutableObjectLink<@Nullable PlayerWrapper> player;
    private final ImmutableObjectLink<Boolean> bonemealed;
    // TODO: Tree type


    public LocationHolder getLocation() {
        return location.get();
    }

    @Nullable
    public PlayerWrapper getPlayer() {
        return player.get();
    }

    public boolean isBonemealed() {
        return bonemealed.get();
    }
}
