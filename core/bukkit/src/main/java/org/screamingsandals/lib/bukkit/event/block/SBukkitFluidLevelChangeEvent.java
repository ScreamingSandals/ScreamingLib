package org.screamingsandals.lib.bukkit.event.block;

import lombok.*;
import org.bukkit.event.block.FluidLevelChangeEvent;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.block.SFluidLevelChangeEvent;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitFluidLevelChangeEvent implements SFluidLevelChangeEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
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
