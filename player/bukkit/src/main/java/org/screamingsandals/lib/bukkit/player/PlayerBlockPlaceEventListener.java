package org.screamingsandals.lib.bukkit.player;

import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.event.SPlayerBlockPlaceEvent;

public class PlayerBlockPlaceEventListener extends AbstractBukkitEventHandlerFactory<BlockPlaceEvent, SPlayerBlockPlaceEvent> {

    public PlayerBlockPlaceEventListener(Plugin plugin) {
        super(BlockPlaceEvent.class, SPlayerBlockPlaceEvent.class, plugin);
    }

    @Override
    protected SPlayerBlockPlaceEvent wrapEvent(BlockPlaceEvent event, EventPriority priority) {
        return null;
    }

    @Override
    protected void handleResult(SPlayerBlockPlaceEvent wrappedEvent, BlockPlaceEvent event) {

    }
}
