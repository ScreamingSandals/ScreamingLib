package org.screamingsandals.lib.bukkit.event.block;

import lombok.Data;
import org.bukkit.event.block.BlockFromToEvent;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.block.SBlockFromToEvent;
import org.screamingsandals.lib.utils.BlockFace;

@Data
public class SBukkitBlockFromToEvent implements SBlockFromToEvent, BukkitCancellable {
    private final BlockFromToEvent event;

    // Internal cache
    private BlockHolder sourceBlock;
    private BlockHolder facedBlock;
    private BlockFace face;

    @Override
    public BlockHolder getSourceBlock() {
        if (sourceBlock == null) {
            sourceBlock = BlockMapper.wrapBlock(event.getBlock());
        }
        return sourceBlock;
    }

    @Override
    public BlockHolder getFacedBlock() {
        if (facedBlock == null) {
            facedBlock = BlockMapper.wrapBlock(event.getToBlock());
        }
        return facedBlock;
    }

    @Override
    public BlockFace getFace() {
        if (face == null) {
            face = BlockFace.valueOf(event.getFace().name());
        }
        return face;
    }
}
