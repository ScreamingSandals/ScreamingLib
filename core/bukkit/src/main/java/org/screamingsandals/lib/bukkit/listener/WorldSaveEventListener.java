package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.bukkit.world.BukkitWorldHolder;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.world.SWorldSaveEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

public class WorldSaveEventListener extends AbstractBukkitEventHandlerFactory<WorldSaveEvent, SWorldSaveEvent> {

    public WorldSaveEventListener(Plugin plugin) {
        super(WorldSaveEvent.class, SWorldSaveEvent.class, plugin);
    }

    @Override
    protected SWorldSaveEvent wrapEvent(WorldSaveEvent event, EventPriority priority) {
        return new SWorldSaveEvent(
                ImmutableObjectLink.of(() -> new BukkitWorldHolder(event.getWorld()))
        );
    }
}
