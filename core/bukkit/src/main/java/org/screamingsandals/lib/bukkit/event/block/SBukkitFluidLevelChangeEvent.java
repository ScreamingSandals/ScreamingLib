package org.screamingsandals.lib.bukkit.event.block;

import lombok.Data;
import org.bukkit.event.block.FluidLevelChangeEvent;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.block.SFluidLevelChangeEvent;

@Data
public class SBukkitFluidLevelChangeEvent implements SFluidLevelChangeEvent, BukkitCancellable {
    private final FluidLevelChangeEvent event;

    // Internal cache
    private BlockHolder block;
    private BlockTypeHolder blockType;

    @Override
    public BlockHolder getBlock() {
        if (block == null) {
            block = BlockMapper.wrapBlock(event.getBlock());
        }
        return block;
    }

    @Override
    public BlockTypeHolder getNewBlockData() {
        if (blockType == null) {
            blockType = BlockTypeHolder.of(event.getNewData());
        }
        return blockType;
    }
}
