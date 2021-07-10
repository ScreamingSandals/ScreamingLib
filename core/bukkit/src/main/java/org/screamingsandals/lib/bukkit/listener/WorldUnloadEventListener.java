package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.bukkit.world.BukkitWorldHolder;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.world.SWorldUnloadEvent;

public class WorldUnloadEventListener extends AbstractBukkitEventHandlerFactory<WorldUnloadEvent, SWorldUnloadEvent> {

    public WorldUnloadEventListener(Plugin plugin) {
        super(WorldUnloadEvent.class, SWorldUnloadEvent.class, plugin);
    }

    @Override
    protected SWorldUnloadEvent wrapEvent(WorldUnloadEvent event, EventPriority priority) {
        return new SWorldUnloadEvent(
                new BukkitWorldHolder(event.getWorld())
        );
    }
}
