package org.screamingsandals.lib.bukkit.event.block;

import lombok.Data;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.block.SBlockPhysicsEvent;

@Data
public class SBukkitBlockPhysicsEvent implements SBlockPhysicsEvent, BukkitCancellable {
    private final BlockPhysicsEvent event;

    // Internal cache
    private BlockHolder block;
    private BlockTypeHolder material;
    private BlockHolder causingBlock;

    @Override
    public BlockHolder getBlock() {
        if (block == null) {
            block = BlockMapper.wrapBlock(event.getBlock());
        }
        return block;
    }

    @Override
    public BlockTypeHolder getMaterial() {
        if (material == null) {
            material = BlockTypeHolder.of(event.getChangedType());
        }
        return material;
    }

    @Override
    public BlockHolder getCausingBlock() {
        if (causingBlock == null) {
            causingBlock = BlockMapper.wrapBlock(event.getSourceBlock());
        }
        return causingBlock;
    }
}
