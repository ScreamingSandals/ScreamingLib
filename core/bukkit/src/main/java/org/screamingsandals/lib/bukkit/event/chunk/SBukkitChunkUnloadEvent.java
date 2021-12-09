package org.screamingsandals.lib.bukkit.event.chunk;

import lombok.*;
import org.bukkit.event.Cancellable;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.screamingsandals.lib.bukkit.event.NoAutoCancellable;
import org.screamingsandals.lib.bukkit.world.chunk.BukkitChunkHolder;
import org.screamingsandals.lib.event.chunk.SChunkUnloadEvent;
import org.screamingsandals.lib.world.chunk.ChunkHolder;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitChunkUnloadEvent implements SChunkUnloadEvent, NoAutoCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
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

    // on newer versions the event is not cancellable
    public boolean isCancelled() {
        return event instanceof Cancellable && ((Cancellable) event).isCancelled();
    }

    public void setCancelled(boolean cancelled) {
        if (event instanceof Cancellable) {
            ((Cancellable) event).setCancelled(cancelled);
        }
    }
}
