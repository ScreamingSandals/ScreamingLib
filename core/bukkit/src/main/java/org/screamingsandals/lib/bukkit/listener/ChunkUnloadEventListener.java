package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.chunk.SChunkUnloadEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.world.chunk.ChunkMapper;

public class ChunkUnloadEventListener extends AbstractBukkitEventHandlerFactory<ChunkUnloadEvent, SChunkUnloadEvent> {

    public ChunkUnloadEventListener(Plugin plugin) {
        super(ChunkUnloadEvent.class, SChunkUnloadEvent.class, plugin);
    }

    @Override
    protected SChunkUnloadEvent wrapEvent(ChunkUnloadEvent event, EventPriority priority) {
        return new SChunkUnloadEvent(
                ImmutableObjectLink.of(() -> ChunkMapper.wrapChunk(event.getChunk()).orElseThrow()),
                ObjectLink.of(event::isSaveChunk, event::setSaveChunk)
        );
    }
}
