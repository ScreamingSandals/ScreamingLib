package org.screamingsandals.lib.bukkit.event.chunk;

import lombok.Data;
import org.bukkit.event.world.ChunkLoadEvent;
import org.screamingsandals.lib.bukkit.world.chunk.BukkitChunkHolder;
import org.screamingsandals.lib.event.chunk.SChunkLoadEvent;
import org.screamingsandals.lib.world.chunk.ChunkHolder;

@Data
public class SBukkitChunkLoadEvent implements SChunkLoadEvent {
    private final ChunkLoadEvent event;

    // Internal cache
    private ChunkHolder cachedChunk;

    @Override
    public ChunkHolder getChunk() {
        if (cachedChunk == null) {
            cachedChunk = new BukkitChunkHolder(event.getChunk());
        }
        return cachedChunk;
    }

    @Override
    public boolean isNewChunk() {
        return event.isNewChunk();
    }
}