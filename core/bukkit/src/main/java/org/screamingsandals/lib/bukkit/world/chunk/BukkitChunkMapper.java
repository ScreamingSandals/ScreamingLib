package org.screamingsandals.lib.bukkit.world.chunk;

import org.bukkit.Chunk;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.world.chunk.ChunkHolder;
import org.screamingsandals.lib.world.chunk.ChunkMapper;

import java.util.Optional;

@Service
public class BukkitChunkMapper extends ChunkMapper {
    @Override
    protected Optional<ChunkHolder> wrapChunk0(Object chunk) {
        if (chunk instanceof Chunk) {
            return Optional.of(new BukkitChunkHolder((Chunk) chunk));
        }
        return Optional.empty();
    }
}
