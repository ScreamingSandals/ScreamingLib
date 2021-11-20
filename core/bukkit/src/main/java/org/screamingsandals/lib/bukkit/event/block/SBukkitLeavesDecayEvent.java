package org.screamingsandals.lib.bukkit.event.block;

import lombok.Data;
import org.bukkit.event.block.LeavesDecayEvent;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.block.SLeavesDecayEvent;

@Data
public class SBukkitLeavesDecayEvent implements SLeavesDecayEvent, BukkitCancellable {
    private final LeavesDecayEvent event;

    // Internal cache
    private BlockHolder block;

    @Override
    public BlockHolder getBlock() {
        if (block == null) {
            block = BlockMapper.wrapBlock(event.getBlock());
        }
        return block;
    }
}
