package org.screamingsandals.lib.bukkit.entity.listener;

import org.bukkit.event.entity.EntityPortalExitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.event.SEntityPortalExitEvent;
import org.screamingsandals.lib.event.EventPriority;

public class EntityPortalEnterExitListener extends AbstractBukkitEventHandlerFactory<EntityPortalExitEvent, SEntityPortalExitEvent> {

    public EntityPortalEnterExitListener(Plugin plugin) {
        super(EntityPortalExitEvent.class, SEntityPortalExitEvent.class, plugin);
    }

    @Override
    protected SEntityPortalExitEvent wrapEvent(EntityPortalExitEvent event, EventPriority priority) {
        return new SEntityPortalExitEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                event.getBefore(),
                event.getAfter()
        );
    }

    @Override
    protected void postProcess(SEntityPortalExitEvent wrappedEvent, EntityPortalExitEvent event) {
        event.setAfter((Vector) wrappedEvent.getAfter());
    }
}
