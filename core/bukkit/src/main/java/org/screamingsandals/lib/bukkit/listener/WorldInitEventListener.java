package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.bukkit.world.BukkitWorldHolder;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.world.SWorldInitEvent;

public class WorldInitEventListener extends AbstractBukkitEventHandlerFactory<WorldInitEvent, SWorldInitEvent> {

    public WorldInitEventListener(Plugin plugin) {
        super(WorldInitEvent.class, SWorldInitEvent.class, plugin);
    }

    @Override
    protected SWorldInitEvent wrapEvent(WorldInitEvent event, EventPriority priority) {
        return new SWorldInitEvent(
                new BukkitWorldHolder(event.getWorld())
        );
    }
}
