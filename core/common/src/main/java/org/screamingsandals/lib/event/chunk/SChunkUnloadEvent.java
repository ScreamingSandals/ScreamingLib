package org.screamingsandals.lib.event.chunk;

import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.world.chunk.ChunkHolder;

/**
 * NOTE: This event is cancellable only on old minecraft versions
 */
public interface SChunkUnloadEvent extends SCancellableEvent, PlatformEventWrapper {

    ChunkHolder getChunk();

    boolean isSaveChunk();

    void setSaveChunk(boolean saveChunk);
}
