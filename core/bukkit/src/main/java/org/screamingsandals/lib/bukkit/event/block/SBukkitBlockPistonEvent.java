package org.screamingsandals.lib.bukkit.event.block;

import lombok.Data;
import org.bukkit.event.block.BlockPistonEvent;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.block.SBlockPistonEvent;
import org.screamingsandals.lib.utils.BlockFace;

@Data
public class SBukkitBlockPistonEvent implements SBlockPistonEvent, BukkitCancellable {
    private final BlockPistonEvent event;

    // Internal cache
    private BlockHolder block;
    private BlockFace direction;

    @Override
    public BlockHolder getBlock() {
        if (block == null) {
            block = BlockMapper.wrapBlock(event.getBlock());
        }
        return block;
    }

    @Override
    public boolean isSticky() {
        return event.isSticky();
    }

    @Override
    public BlockFace getDirection() {
        if (direction == null) {
            direction = BlockFace.valueOf(event.getDirection().name());
        }
        return direction;
    }
}
