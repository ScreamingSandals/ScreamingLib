package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SCreatureSpawnEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

public class CreatureSpawnEventListener extends AbstractBukkitEventHandlerFactory<CreatureSpawnEvent, SCreatureSpawnEvent> {

    public CreatureSpawnEventListener(Plugin plugin) {
        super(CreatureSpawnEvent.class, SCreatureSpawnEvent.class, plugin);
    }

    @Override
    protected SCreatureSpawnEvent wrapEvent(CreatureSpawnEvent event, EventPriority priority) {
        return new SCreatureSpawnEvent(
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                ImmutableObjectLink.of(() -> SCreatureSpawnEvent.SpawnReason.valueOf(event.getSpawnReason().name().toUpperCase()))
        );
    }
}
