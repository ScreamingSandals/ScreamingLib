package org.screamingsandals.lib.bukkit.event.block;

import lombok.Data;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockExplodeEvent;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.block.SBlockExplodeEvent;
import org.screamingsandals.lib.utils.CollectionLinkedToCollection;

import java.util.Collection;

@Data
public class SBukkitBlockExplodeEvent implements SBlockExplodeEvent, BukkitCancellable {
    private final BlockExplodeEvent event;

    // Internal cache
    private BlockHolder block;
    private Collection<BlockHolder> destroyed;

    @Override
    public BlockHolder getBlock() {
        if (block == null) {
            block = BlockMapper.wrapBlock(event.getBlock());
        }
        return block;
    }

    @Override
    public Collection<BlockHolder> getDestroyed() {
        if (destroyed == null) {
            destroyed = new CollectionLinkedToCollection<>(event.blockList(), o -> o.as(Block.class), BlockMapper::wrapBlock);
        }
        return destroyed;
    }

    @Override
    public float getYield() {
        return event.getYield();
    }

    @Override
    public void setYield(float yield) {
        event.setYield(yield);
    }
}
