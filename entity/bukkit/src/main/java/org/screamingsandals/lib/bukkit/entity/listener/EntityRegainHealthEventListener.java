package org.screamingsandals.lib.bukkit.entity.listener;

import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.event.SEntityRegainHealthEvent;
import org.screamingsandals.lib.event.EventPriority;

public class EntityRegainHealthEventListener extends AbstractBukkitEventHandlerFactory<EntityRegainHealthEvent, SEntityRegainHealthEvent> {

    public EntityRegainHealthEventListener(Plugin plugin) {
        super(EntityRegainHealthEvent.class, SEntityRegainHealthEvent.class, plugin);
    }

    @Override
    protected SEntityRegainHealthEvent wrapEvent(EntityRegainHealthEvent event, EventPriority priority) {
        return new SEntityRegainHealthEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                event.getAmount(),
                SEntityRegainHealthEvent.RegainReason.valueOf(event.getRegainReason().name().toUpperCase())
        );
    }
}
