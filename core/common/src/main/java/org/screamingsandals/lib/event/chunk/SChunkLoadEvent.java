package org.screamingsandals.lib.event.chunk;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.world.chunk.ChunkHolder;

@EqualsAndHashCode(callSuper = true)
@Data
public class SChunkLoadEvent extends AbstractEvent {
    private final ImmutableObjectLink<ChunkHolder> chunk;
    private final ImmutableObjectLink<Boolean> newChunk;

    public ChunkHolder getChunk() {
        return chunk.get();
    }

    public Boolean getNewChunk() {
        return newChunk.get();
    }
}
