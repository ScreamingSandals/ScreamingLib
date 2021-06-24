package org.screamingsandals.lib.bukkit.entity.listener;

import org.bukkit.event.entity.EntityExhaustionEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.event.SEntityExhaustionEvent;
import org.screamingsandals.lib.event.EventPriority;

public class EntityExhaustionEventListener extends AbstractBukkitEventHandlerFactory<EntityExhaustionEvent, SEntityExhaustionEvent> {

    public EntityExhaustionEventListener(Plugin plugin) {
        super(EntityExhaustionEvent.class, SEntityExhaustionEvent.class, plugin);
    }

    @Override
    protected SEntityExhaustionEvent wrapEvent(EntityExhaustionEvent event, EventPriority priority) {
        return new SEntityExhaustionEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                SEntityExhaustionEvent.ExhaustionReason.valueOf(event.getExhaustionReason().name().toUpperCase()),
                event.getExhaustion()
        );
    }

    @Override
    protected void postProcess(SEntityExhaustionEvent wrappedEvent, EntityExhaustionEvent event) {
        event.setExhaustion(wrappedEvent.getExhaustion());
    }
}
