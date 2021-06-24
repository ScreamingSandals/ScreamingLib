package org.screamingsandals.lib.bukkit.entity.listener;

import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.event.SEntityExplodeEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.world.BlockMapper;
import org.screamingsandals.lib.world.LocationMapper;

import java.util.stream.Collectors;

public class EntityExplodeEventListener extends AbstractBukkitEventHandlerFactory<EntityExplodeEvent, SEntityExplodeEvent> {

    public EntityExplodeEventListener(Plugin plugin) {
        super(EntityExplodeEvent.class, SEntityExplodeEvent.class, plugin);
    }

    @Override
    protected SEntityExplodeEvent wrapEvent(EntityExplodeEvent event, EventPriority priority) {
        return new SEntityExplodeEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                LocationMapper.wrapLocation(event.getLocation()),
                event
                        .blockList()
                        .stream()
                        .map(BlockMapper::wrapBlock)
                        .collect(Collectors.toList()),
                event.getYield()
        );
    }
}
