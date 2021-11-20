package org.screamingsandals.lib.bukkit.event.block;

import org.bukkit.event.block.EntityBlockFormEvent;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.block.SBlockFormedByEntityEvent;

public class SBukkitBlockFormedByEntityEvent extends SBukkitBlockFormEvent implements SBlockFormedByEntityEvent {
    // Internal cache
    private EntityBasic producer;

    public SBukkitBlockFormedByEntityEvent(EntityBlockFormEvent event) {
        super(event);
    }

    @Override
    public EntityBasic getProducer() {
        if (producer == null) {
            producer = EntityMapper.wrapEntity(((EntityBlockFormEvent) getEvent()).getEntity()).orElseThrow();
        }
        return producer;
    }
}
