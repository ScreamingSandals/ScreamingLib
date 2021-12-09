package org.screamingsandals.lib.bukkit.event.chunk;

import lombok.*;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.screamingsandals.lib.bukkit.world.chunk.BukkitChunkHolder;
import org.screamingsandals.lib.event.chunk.SChunkPopulateEvent;
import org.screamingsandals.lib.world.chunk.ChunkHolder;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitChunkPopulateEvent implements SChunkPopulateEvent {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
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
