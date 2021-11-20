package org.screamingsandals.lib.bukkit.event.chunk;

import lombok.Data;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.screamingsandals.lib.bukkit.world.chunk.BukkitChunkHolder;
import org.screamingsandals.lib.event.chunk.SChunkPopulateEvent;
import org.screamingsandals.lib.world.chunk.ChunkHolder;

@Data
public class SBukkitChunkPopulateEvent implements SChunkPopulateEvent {
    private final ChunkPopulateEvent event;

    // Internal cache
    private ChunkHolder cachedChunk;

    @Override
    public ChunkHolder getChunk() {
        if (cachedChunk == null) {
            cachedChunk = new BukkitChunkHolder(event.getChunk());
        }
        return cachedChunk;
    }
}
