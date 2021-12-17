package org.screamingsandals.lib.bukkit.event.entity;

import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityCombustByEntityEvent;

public class SBukkitEntityCombustByEntityEvent extends SBukkitEntityCombustEvent implements SEntityCombustByEntityEvent {
    public SBukkitEntityCombustByEntityEvent(EntityCombustByEntityEvent event) {
        super(event);
    }

    // Internal cache
    private EntityBasic combuster;

    @Override
    public EntityBasic getCombuster() {
        if (combuster == null) {
            combuster = EntityMapper.wrapEntity(((EntityCombustByEntityEvent) getEvent()).getCombuster()).orElseThrow();
        }
        return combuster;
    }
}
