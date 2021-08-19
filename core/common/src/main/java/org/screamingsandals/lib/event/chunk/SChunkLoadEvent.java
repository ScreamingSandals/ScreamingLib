package org.screamingsandals.lib.event.chunk;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.world.chunk.ChunkHolder;

@EqualsAndHashCode(callSuper = true)
@Data
public class SChunkLoadEvent extends AbstractEvent {
    private final ChunkHolder chunk;
    private final boolean newChunk;
}
