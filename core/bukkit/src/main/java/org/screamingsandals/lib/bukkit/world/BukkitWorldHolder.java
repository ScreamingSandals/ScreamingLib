package org.screamingsandals.lib.bukkit.world;

import org.bukkit.Location;
import org.bukkit.World;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.WorldHolder;
import org.screamingsandals.lib.world.chunk.ChunkHolder;
import org.screamingsandals.lib.world.chunk.ChunkMapper;
import org.screamingsandals.lib.world.difficulty.DifficultyHolder;
import org.screamingsandals.lib.world.dimension.DimensionHolder;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Override
    public List<EntityBasic> getEntities() {
        return wrappedObject.getEntities().stream()
                .map(EntityMapper::wrapEntity)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
