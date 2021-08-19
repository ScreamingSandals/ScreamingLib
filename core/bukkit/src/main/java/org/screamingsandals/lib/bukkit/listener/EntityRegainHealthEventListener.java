package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityRegainHealthEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

public class EntityRegainHealthEventListener extends AbstractBukkitEventHandlerFactory<EntityRegainHealthEvent, SEntityRegainHealthEvent> {

    public EntityRegainHealthEventListener(Plugin plugin) {
        super(EntityRegainHealthEvent.class, SEntityRegainHealthEvent.class, plugin);
    }

    @Override
    protected SEntityRegainHealthEvent wrapEvent(EntityRegainHealthEvent event, EventPriority priority) {
        return new SEntityRegainHealthEvent(
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                ObjectLink.of(event::getAmount, event::setAmount),
                ImmutableObjectLink.of(() -> SEntityRegainHealthEvent.RegainReason.valueOf(event.getRegainReason().name().toUpperCase()))
        );
    }
}
