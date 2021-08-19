package org.screamingsandals.lib.event.chunk;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.world.chunk.ChunkHolder;

@EqualsAndHashCode(callSuper = true)
@Data
public class SChunkPopulateEvent extends AbstractEvent {
    private final ImmutableObjectLink<ChunkHolder> chunk;

    public ChunkHolder getChunk() {
        return chunk.get();
    }
}
