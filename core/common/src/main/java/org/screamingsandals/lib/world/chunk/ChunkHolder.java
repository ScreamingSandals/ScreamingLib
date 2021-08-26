package org.screamingsandals.lib.world.chunk;

import org.jetbrains.annotations.Range;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.world.WorldHolder;

public interface ChunkHolder extends Wrapper {
    int getX();

    int getZ();

    WorldHolder getWorld();

    BlockHolder getBlock(@Range(from = 0, to = 15) int x, int y, @Range(from = 0, to = 15) int z);

    EntityBasic[] getEntities();

    boolean isLoaded();

    boolean load();

    boolean load(boolean generate);

    boolean unload();

    boolean unload(boolean save);
}
