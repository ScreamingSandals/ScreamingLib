package org.screamingsandals.lib.event.chunk;

import org.screamingsandals.lib.event.SEvent;
import org.screamingsandals.lib.world.chunk.ChunkHolder;

public interface SChunkPopulateEvent extends SEvent {

    ChunkHolder getChunk();
}
