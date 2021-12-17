package org.screamingsandals.lib.bukkit.event.entity;

import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.event.entity.SEntityCombustByBlockEvent;

public class SBukkitEntityCombustByBlockEvent extends SBukkitEntityCombustEvent implements SEntityCombustByBlockEvent {
    public SBukkitEntityCombustByBlockEvent(EntityCombustByBlockEvent event) {
        super(event);
    }

    // Internal cache
    private BlockHolder combuster;

    @Override
    public BlockHolder getCombuster() {
        if (combuster == null) {
            combuster = BlockMapper.wrapBlock(((EntityCombustByBlockEvent) getEvent()).getCombuster());
        }
        return combuster;
    }
}
