package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.EntityEnterLoveModeEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityEnterLoveModeEvent;
import org.screamingsandals.lib.event.EventPriority;

public class EntityEnterLoveModeEventListener extends AbstractBukkitEventHandlerFactory<EntityEnterLoveModeEvent, SEntityEnterLoveModeEvent> {

    public EntityEnterLoveModeEventListener(Plugin plugin) {
        super(EntityEnterLoveModeEvent.class, SEntityEnterLoveModeEvent.class, plugin);
    }

    @Override
    protected SEntityEnterLoveModeEvent wrapEvent(EntityEnterLoveModeEvent event, EventPriority priority) {
        return new SEntityEnterLoveModeEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                EntityMapper.wrapEntity(event.getHumanEntity()).orElseThrow(),
                event.getTicksInLove()
        );
    }

    @Override
    protected void postProcess(SEntityEnterLoveModeEvent wrappedEvent, EntityEnterLoveModeEvent event) {
        event.setTicksInLove(wrappedEvent.getTicksInLove());
    }
}
