package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.world.SpawnChangeEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.bukkit.world.BukkitWorldHolder;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.world.LocationMapper;
import org.screamingsandals.lib.event.world.SSpawnChangeEvent;

public class SpawnChangeEventListener extends AbstractBukkitEventHandlerFactory<SpawnChangeEvent, SSpawnChangeEvent> {

    public SpawnChangeEventListener(Plugin plugin) {
        super(SpawnChangeEvent.class, SSpawnChangeEvent.class, plugin);
    }

    @Override
    protected SSpawnChangeEvent wrapEvent(SpawnChangeEvent event, EventPriority priority) {
        return new SSpawnChangeEvent(
                new BukkitWorldHolder(event.getWorld()),
                LocationMapper.wrapLocation(event.getPreviousLocation()),
                LocationMapper.wrapLocation(event.getWorld().getSpawnLocation())
        );
    }
}
