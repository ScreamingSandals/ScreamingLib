package org.screamingsandals.lib.event.chunk;

import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SEvent;
import org.screamingsandals.lib.world.chunk.ChunkHolder;

public interface SChunkLoadEvent extends SEvent, PlatformEventWrapper {

    ChunkHolder getChunk();

    boolean isNewChunk();
}
