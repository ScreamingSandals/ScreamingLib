package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.bukkit.world.BukkitWorldHolder;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.world.SWorldLoadEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

public class WorldLoadEventListener extends AbstractBukkitEventHandlerFactory<WorldLoadEvent, SWorldLoadEvent> {

    public WorldLoadEventListener(Plugin plugin) {
        super(WorldLoadEvent.class, SWorldLoadEvent.class, plugin);
    }

    @Override
    protected SWorldLoadEvent wrapEvent(WorldLoadEvent event, EventPriority priority) {
        return new SWorldLoadEvent(
                ImmutableObjectLink.of(() -> new BukkitWorldHolder(event.getWorld()))
        );
    }
}
