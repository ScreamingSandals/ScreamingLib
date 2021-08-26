package org.screamingsandals.lib.gamecore.region;

import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.world.BlockHolder;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.block.state.BlockStateHolder;

public interface Region {
    boolean isBlockAddedDuringGame(LocationHolder loc);

    void putOriginalBlock(LocationHolder loc, BlockStateHolder block);

    void addBuiltDuringGame(LocationHolder loc);

    void removeBlockBuiltDuringGame(LocationHolder loc);

    boolean isLiquid(MaterialHolder material);

    boolean isBedBlock(BlockStateHolder block);

    boolean isBedHead(BlockStateHolder block);

    BlockHolder getBedNeighbor(BlockHolder head);

    //boolean isChunkUsed(Chunk chunk); // TODO

    void regen();
}
