package org.screamingsandals.lib.event.chunk;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.world.chunk.ChunkHolder;

/**
 * NOTE: This event is cancellable only on old minecraft versions
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class SChunkUnloadEvent extends CancellableAbstractEvent {

    public abstract ChunkHolder getChunk();

    public abstract boolean isSaveChunk();

    public abstract void setSaveChunk(boolean saveChunk);
}
