package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.bukkit.event.chunk.SBukkitChunkPopulateEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.chunk.SChunkPopulateEvent;

public class ChunkPopulateEventListener extends AbstractBukkitEventHandlerFactory<ChunkPopulateEvent, SChunkPopulateEvent> {

    public ChunkPopulateEventListener(Plugin plugin) {
        super(ChunkPopulateEvent.class, SChunkPopulateEvent.class, plugin);
    }

    @Override
    protected SChunkPopulateEvent wrapEvent(ChunkPopulateEvent event, EventPriority priority) {
        return new SBukkitChunkPopulateEvent(event);
    }
}
