package org.screamingsandals.lib.bukkit.world.chunk;

import org.bukkit.Chunk;
import org.jetbrains.annotations.Range;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.world.WorldHolder;
import org.screamingsandals.lib.world.WorldMapper;
import org.screamingsandals.lib.world.chunk.ChunkHolder;

import java.util.Arrays;
import java.util.Optional;

public class BukkitChunkHolder extends BasicWrapper<Chunk> implements ChunkHolder {
    public BukkitChunkHolder(Chunk wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public int getX() {
        return wrappedObject.getX();
    }

    @Override
    public int getZ() {
        return wrappedObject.getZ();
    }

    @Override
    public WorldHolder getWorld() {
        return WorldMapper.wrapWorld(wrappedObject.getWorld());
    }

    @Override
    public BlockHolder getBlock(@Range(from = 0, to = 15) int x, int y, @Range(from = 0, to = 15) int z) {
        return BlockMapper.wrapBlock(wrappedObject.getBlock(x, y, z));
    }

    @Override
    public EntityBasic[] getEntities() {
        return Arrays.stream(wrappedObject.getEntities())
                .map(EntityMapper::wrapEntity)
                .filter(Optional::isPresent)
                .map(Optional::orElseThrow)
                .toArray(EntityBasic[]::new);
    }

    @Override
    public boolean isLoaded() {
        return wrappedObject.isLoaded();
    }

    @Override
    public boolean load() {
        return wrappedObject.load();
    }

    @Override
    public boolean load(boolean generate) {
        return wrappedObject.load(generate);
    }

    @Override
    public boolean unload() {
        return wrappedObject.unload();
    }

    @Override
    public boolean unload(boolean save) {
        return wrappedObject.unload(save);
    }
}
