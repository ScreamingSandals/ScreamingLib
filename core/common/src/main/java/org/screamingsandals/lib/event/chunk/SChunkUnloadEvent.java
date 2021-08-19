package org.screamingsandals.lib.event.chunk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.world.chunk.ChunkHolder;

/**
 * NOTE: This event is cancellable only on old minecraft versions
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class SChunkUnloadEvent extends CancellableAbstractEvent {
    private final ChunkHolder chunk;
    private boolean saveChunk;
}
