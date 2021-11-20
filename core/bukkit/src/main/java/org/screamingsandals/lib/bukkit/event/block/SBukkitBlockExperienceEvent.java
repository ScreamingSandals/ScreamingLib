package org.screamingsandals.lib.bukkit.event.block;

import lombok.Data;
import org.bukkit.event.block.BlockExpEvent;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.event.block.SBlockExperienceEvent;

@Data
public class SBukkitBlockExperienceEvent implements SBlockExperienceEvent {
    private final BlockExpEvent event;

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
    public int getExperience() {
        return event.getExpToDrop();
    }

    @Override
    public void setExperience(int experience) {
        event.setExpToDrop(experience);
    }
}
