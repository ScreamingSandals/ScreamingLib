package org.screamingsandals.lib.bukkit.event.entity;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityDamageByEntityEvent;

public class SBukkitEntityDamageByEntityEvent extends SBukkitEntityDamageEvent implements SEntityDamageByEntityEvent {
    public SBukkitEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        super(event);
    }

    // Internal cache
    private EntityBasic damager;

    @Override
    public EntityBasic getDamager() {
        if (damager == null) {
            damager = EntityMapper.wrapEntity(((EntityDamageByEntityEvent) getEvent()).getDamager()).orElseThrow();
        }
        return damager;
    }
}
