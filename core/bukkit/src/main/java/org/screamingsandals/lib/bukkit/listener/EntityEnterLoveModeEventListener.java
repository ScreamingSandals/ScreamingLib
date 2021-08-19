package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.EntityEnterLoveModeEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityEnterLoveModeEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

public class EntityEnterLoveModeEventListener extends AbstractBukkitEventHandlerFactory<EntityEnterLoveModeEvent, SEntityEnterLoveModeEvent> {

    public EntityEnterLoveModeEventListener(Plugin plugin) {
        super(EntityEnterLoveModeEvent.class, SEntityEnterLoveModeEvent.class, plugin);
    }

    @Override
    protected SEntityEnterLoveModeEvent wrapEvent(EntityEnterLoveModeEvent event, EventPriority priority) {
        return new SEntityEnterLoveModeEvent(
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getHumanEntity()).orElseThrow()),
                ObjectLink.of(event::getTicksInLove, event::setTicksInLove)
        );
    }
}
