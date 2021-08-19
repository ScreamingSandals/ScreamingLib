package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.EntityExhaustionEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityExhaustionEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

public class EntityExhaustionEventListener extends AbstractBukkitEventHandlerFactory<EntityExhaustionEvent, SEntityExhaustionEvent> {

    public EntityExhaustionEventListener(Plugin plugin) {
        super(EntityExhaustionEvent.class, SEntityExhaustionEvent.class, plugin);
    }

    @Override
    protected SEntityExhaustionEvent wrapEvent(EntityExhaustionEvent event, EventPriority priority) {
        return new SEntityExhaustionEvent(
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                ImmutableObjectLink.of(() -> SEntityExhaustionEvent.ExhaustionReason.valueOf(event.getExhaustionReason().name().toUpperCase())),
                ObjectLink.of(event::getExhaustion, event::setExhaustion)
        );
    }
}
