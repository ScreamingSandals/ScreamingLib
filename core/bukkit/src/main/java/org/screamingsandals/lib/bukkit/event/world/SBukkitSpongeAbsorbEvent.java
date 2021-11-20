package org.screamingsandals.lib.bukkit.event.world;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.block.BlockState;
import org.bukkit.event.block.SpongeAbsorbEvent;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.block.state.BlockStateHolder;
import org.screamingsandals.lib.block.state.BlockStateMapper;
import org.screamingsandals.lib.event.world.SSpongeAbsorbEvent;
import org.screamingsandals.lib.utils.CollectionLinkedToCollection;

import java.util.Collection;

@Data
@EqualsAndHashCode(callSuper = true)
public class SBukkitSpongeAbsorbEvent extends SSpongeAbsorbEvent {
    private final SpongeAbsorbEvent event;

    // Internal cache
    private BlockHolder block;
    private Collection<BlockStateHolder> waterBlocks;

    @Override
    public BlockHolder getBlock() {
        if (block == null) {
            block = BlockMapper.wrapBlock(event.getBlock());
        }
        return block;
    }

    @Override
    public Collection<BlockStateHolder> getWaterBlocks() {
        if (waterBlocks == null) {
            waterBlocks = new CollectionLinkedToCollection<>(event.getBlocks(), o -> o.as(BlockState.class), o -> BlockStateMapper.wrapBlockState(o).orElseThrow());
        }
        return waterBlocks;
    }
}
