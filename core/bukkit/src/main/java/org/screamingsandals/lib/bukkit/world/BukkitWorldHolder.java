package org.screamingsandals.lib.bukkit.world;

import org.bukkit.Location;
import org.bukkit.World;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.WorldHolder;
import org.screamingsandals.lib.world.chunk.ChunkHolder;
import org.screamingsandals.lib.world.chunk.ChunkMapper;
import org.screamingsandals.lib.world.difficulty.DifficultyHolder;
import org.screamingsandals.lib.world.dimension.DimensionHolder;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.Optional;
import java.util.UUID;

@ConfigSerializable
public class BukkitWorldHolder extends BasicWrapper<World> implements WorldHolder {

    public BukkitWorldHolder(World wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public UUID getUuid() {
        return wrappedObject.getUID();
    }

    @Override
    public String getName() {
        return wrappedObject.getName();
    }

    @Override
    public int getMinY() {
        if (Reflect.hasMethod(wrappedObject, "getMinHeight")) {
            return wrappedObject.getMinHeight();
        } else {
            return 0;
        }
    }

    @Override
    public int getMaxY() {
        return wrappedObject.getMaxHeight();
    }

    @Override
    public DifficultyHolder getDifficulty() {
        return DifficultyHolder.of(wrappedObject.getDifficulty());
    }

    @Override
    public DimensionHolder getDimension() {
        return DimensionHolder.of(wrappedObject.getEnvironment());
    }

    @Override
    public Optional<ChunkHolder> getChunkAt(int x, int z) {
        return ChunkMapper.wrapChunk(wrappedObject.getChunkAt(x, z));
    }

    @Override
    public Optional<ChunkHolder> getChunkAt(LocationHolder location) {
        return ChunkMapper.wrapChunk(wrappedObject.getChunkAt(location.as(Location.class)));
    }

}
