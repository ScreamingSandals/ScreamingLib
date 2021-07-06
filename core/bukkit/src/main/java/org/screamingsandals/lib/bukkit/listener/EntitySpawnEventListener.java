package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntitySpawnEvent;
import org.screamingsandals.lib.event.entity.SItemSpawnEvent;
import org.screamingsandals.lib.event.entity.SProjectileLaunchEvent;
import org.screamingsandals.lib.event.EventPriority;

public class EntitySpawnEventListener extends AbstractBukkitEventHandlerFactory<EntitySpawnEvent, SEntitySpawnEvent> {

    public EntitySpawnEventListener(Plugin plugin) {
        super(EntitySpawnEvent.class, SEntitySpawnEvent.class, plugin);
    }

    @Override
    protected SEntitySpawnEvent wrapEvent(EntitySpawnEvent event, EventPriority priority) {
        if (event instanceof ItemSpawnEvent) {
            return new SItemSpawnEvent(EntityMapper.wrapEntity(event.getEntity()).orElseThrow());
        }
        if (event instanceof ProjectileLaunchEvent) {
            return new SProjectileLaunchEvent(EntityMapper.wrapEntity(event.getEntity()).orElseThrow());
        }
        return new SEntitySpawnEvent(EntityMapper.wrapEntity(event.getEntity()).orElseThrow());
    }
}
