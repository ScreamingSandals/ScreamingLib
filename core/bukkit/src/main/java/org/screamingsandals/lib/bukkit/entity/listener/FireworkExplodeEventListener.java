package org.screamingsandals.lib.bukkit.entity.listener;

import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.event.SFireworkExplodeEvent;
import org.screamingsandals.lib.event.EventPriority;

public class FireworkExplodeEventListener extends AbstractBukkitEventHandlerFactory<FireworkExplodeEvent, SFireworkExplodeEvent> {

    public FireworkExplodeEventListener(Plugin plugin) {
        super(FireworkExplodeEvent.class, SFireworkExplodeEvent.class, plugin);
    }

    @Override
    protected SFireworkExplodeEvent wrapEvent(FireworkExplodeEvent event, EventPriority priority) {
        return new SFireworkExplodeEvent(EntityMapper.wrapEntity(event.getEntity()).orElseThrow());
    }
}
