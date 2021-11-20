package org.screamingsandals.lib.bukkit.event.block;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.event.block.MoistureChangeEvent;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.block.state.BlockStateHolder;
import org.screamingsandals.lib.block.state.BlockStateMapper;
import org.screamingsandals.lib.event.block.SMoistureChangeEvent;

@Data
@EqualsAndHashCode(callSuper = true)
public class SBukkitMoistureChangeEvent extends SMoistureChangeEvent {
    private final MoistureChangeEvent event;

    // Internal cache
    private BlockHolder block;
    private BlockStateHolder newBlockState;

    @Override
    public BlockHolder getBlock() {
        if (block == null) {
            block = BlockMapper.wrapBlock(event.getBlock());
        }
        return block;
    }

    @Override
    public BlockStateHolder getNewBlockState() {
        if (newBlockState == null) {
            newBlockState = BlockStateMapper.wrapBlockState(event.getNewState()).orElseThrow();
        }
        return newBlockState;
    }
}
