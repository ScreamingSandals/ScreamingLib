package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.world.SpawnChangeEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.bukkit.world.BukkitWorldHolder;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.world.LocationMapper;
import org.screamingsandals.lib.event.world.SSpawnChangeEvent;

public class SpawnChangeEventListener extends AbstractBukkitEventHandlerFactory<SpawnChangeEvent, SSpawnChangeEvent> {

    public SpawnChangeEventListener(Plugin plugin) {
        super(SpawnChangeEvent.class, SSpawnChangeEvent.class, plugin);
    }

    @Override
    protected SSpawnChangeEvent wrapEvent(SpawnChangeEvent event, EventPriority priority) {
        return new SSpawnChangeEvent(
                ImmutableObjectLink.of(() -> new BukkitWorldHolder(event.getWorld())),
                ImmutableObjectLink.of(() -> LocationMapper.wrapLocation(event.getPreviousLocation())),
                ImmutableObjectLink.of(() -> LocationMapper.wrapLocation(event.getWorld().getSpawnLocation()))
        );
    }
}
