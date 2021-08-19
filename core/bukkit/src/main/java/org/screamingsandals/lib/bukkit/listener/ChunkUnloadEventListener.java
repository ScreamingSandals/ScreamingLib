package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.chunk.SChunkUnloadEvent;
import org.screamingsandals.lib.world.chunk.ChunkMapper;

public class ChunkUnloadEventListener extends AbstractBukkitEventHandlerFactory<ChunkUnloadEvent, SChunkUnloadEvent> {

    public ChunkUnloadEventListener(Plugin plugin) {
        super(ChunkUnloadEvent.class, SChunkUnloadEvent.class, plugin);
    }

    @Override
    protected SChunkUnloadEvent wrapEvent(ChunkUnloadEvent event, EventPriority priority) {
        return new SChunkUnloadEvent(
                ChunkMapper.wrapChunk(event.getChunk()).orElseThrow(),
                event.isSaveChunk()
        );
    }

    @Override
    protected void postProcess(SChunkUnloadEvent wrappedEvent, ChunkUnloadEvent event) {
        event.setSaveChunk(wrappedEvent.isSaveChunk());
    }
}
