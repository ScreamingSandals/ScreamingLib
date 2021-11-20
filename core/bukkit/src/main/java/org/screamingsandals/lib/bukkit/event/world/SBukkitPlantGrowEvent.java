package org.screamingsandals.lib.bukkit.event.world;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.block.BlockState;
import org.bukkit.event.world.StructureGrowEvent;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.state.BlockStateHolder;
import org.screamingsandals.lib.block.state.BlockStateMapper;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.event.world.SPlantGrowEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.CollectionLinkedToCollection;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;

import java.util.Collection;

@Data
@EqualsAndHashCode(callSuper = true)
public class SBukkitPlantGrowEvent extends SPlantGrowEvent {
    private final StructureGrowEvent event;

    // Internal cache
    private Collection<BlockStateHolder> collection;
    private LocationHolder location;
    private PlayerWrapper player;
    private boolean playerCached;

    @Override
    public Collection<BlockStateHolder> getBlockStates() {
        if (collection == null) {
            collection = new CollectionLinkedToCollection<>(event.getBlocks(), o -> o.as(BlockState.class), o -> BlockStateMapper.wrapBlockState(o).orElseThrow());
        }
        return collection;
    }

    @Override
    public LocationHolder getLocation() {
        if (location == null) {
            location = LocationMapper.wrapLocation(event.getLocation());
        }
        return location;
    }

    @Override
    public @Nullable PlayerWrapper getPlayer() {
        if (!playerCached) {
            if (event.getPlayer() != null) {
                player = new BukkitEntityPlayer(event.getPlayer());
            }
            playerCached = true;
        }
        return player;
    }

    @Override
    public boolean isBonemealed() {
        return event.isFromBonemeal();
    }
}
