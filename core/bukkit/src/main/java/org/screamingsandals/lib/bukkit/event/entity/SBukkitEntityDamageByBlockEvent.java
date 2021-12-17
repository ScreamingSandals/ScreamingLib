package org.screamingsandals.lib.bukkit.event.entity;

import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.event.entity.SEntityDamageByBlockEvent;

public class SBukkitEntityDamageByBlockEvent extends SBukkitEntityDamageEvent implements SEntityDamageByBlockEvent {
    public SBukkitEntityDamageByBlockEvent(EntityDamageByBlockEvent event) {
        super(event);
    }

    // Internal cache
    private BlockHolder damager;
    private boolean damagerCached;

    @Override
    @Nullable
    public BlockHolder getDamager() {
        if (!damagerCached) {
            if (getEvent().getDamager() != null) {
                damager = BlockMapper.wrapBlock(getEvent().getDamager());
            }
            damagerCached = true;
        }
        return damager;
    }

    @Override
    public EntityDamageByBlockEvent getEvent() {
        return (EntityDamageByBlockEvent) super.getEvent();
    }
}
