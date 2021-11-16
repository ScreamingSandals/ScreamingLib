package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.bukkit.event.chunk.SBukkitChunkLoadEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.chunk.SChunkLoadEvent;

public class ChunkLoadEventListener extends AbstractBukkitEventHandlerFactory<ChunkLoadEvent, SChunkLoadEvent> {

    public ChunkLoadEventListener(Plugin plugin) {
        super(ChunkLoadEvent.class, SChunkLoadEvent.class, plugin);
    }

    @Override
    protected SChunkLoadEvent wrapEvent(ChunkLoadEvent event, EventPriority priority) {
        return new SBukkitChunkLoadEvent(event);
    }
}
