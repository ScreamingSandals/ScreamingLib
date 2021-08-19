package org.screamingsandals.lib.event.chunk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.world.chunk.ChunkHolder;

/**
 * NOTE: This event is cancellable only on old minecraft versions
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class SChunkUnloadEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<ChunkHolder> chunk;
    private final ObjectLink<Boolean> saveChunk;

    public ChunkHolder getChunk() {
        return chunk.get();
    }

    public boolean isSaveChunk() {
        return saveChunk.get();
    }

    public void setSaveChunk(boolean saveChunk) {
        this.saveChunk.set(saveChunk);
    }
}
