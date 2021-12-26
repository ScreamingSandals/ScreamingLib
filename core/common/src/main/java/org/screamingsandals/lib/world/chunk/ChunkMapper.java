package org.screamingsandals.lib.world.chunk;

import org.jetbrains.annotations.ApiStatus;
import org.screamingsandals.lib.utils.annotations.AbstractService;

import java.util.Optional;

@AbstractService(
        pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+\\.[^\\.]+)\\.(?<className>.+)$"
)
public abstract class ChunkMapper {

    private static ChunkMapper chunkMapper;

    @ApiStatus.Internal
    public ChunkMapper() {
        if (chunkMapper != null) {
            throw new UnsupportedOperationException("ChunkMapper is already initialized.");
        }

        chunkMapper = this;
    }

    public static Optional<ChunkHolder> wrapChunk(Object chunk) {
        if (chunkMapper == null) {
            throw new UnsupportedOperationException("ChunkMapper is not initialized yet.");
        }
        if (chunk instanceof ChunkHolder) {
            return Optional.of((ChunkHolder) chunk);
        }
        return chunkMapper.wrapChunk0(chunk);
    }

    protected abstract Optional<ChunkHolder> wrapChunk0(Object chunk);
}
