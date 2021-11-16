package org.screamingsandals.lib.bukkit.event.chunk;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.screamingsandals.lib.bukkit.world.chunk.BukkitChunkHolder;
import org.screamingsandals.lib.event.chunk.SChunkUnloadEvent;
import org.screamingsandals.lib.world.chunk.ChunkHolder;

@EqualsAndHashCode(callSuper = true)
@Data
public class SBukkitChunkUnloadEvent extends SChunkUnloadEvent {
    private final ChunkUnloadEvent event;

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
    public boolean isSaveChunk() {
        return event.isSaveChunk();
    }

    @Override
    public void setSaveChunk(boolean saveChunk) {
        event.setSaveChunk(saveChunk);
    }
}
