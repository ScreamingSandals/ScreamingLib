package org.screamingsandals.lib.bukkit.event.block;

import lombok.Data;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.jetbrains.annotations.Range;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.block.SRedstoneEvent;

@Data
public class SBukkitRedstoneEvent implements SRedstoneEvent, BukkitCancellable {
    private final BlockRedstoneEvent event;

    // Internal cache
    private BlockHolder block;

    @Override
    public BlockHolder getBlock() {
        if (block == null) {
            block = BlockMapper.wrapBlock(event.getBlock());
        }
        return block;
    }

    @Override
    public @Range(from = 0, to = 15) int getOldCurrent() {
        return event.getOldCurrent();
    }

    @Override
    public @Range(from = 0, to = 15) int getNewCurrent() {
        return event.getNewCurrent();
    }

    @Override
    public void setNewCurrent(@Range(from = 0, to = 15) int newCurrent) {
        event.setNewCurrent(newCurrent);
    }
}
