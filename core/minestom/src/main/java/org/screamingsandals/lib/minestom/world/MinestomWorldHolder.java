package org.screamingsandals.lib.minestom.world;

import net.kyori.adventure.audience.Audience;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.particle.ParticleHolder;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.WorldHolder;
import org.screamingsandals.lib.world.chunk.ChunkHolder;
import org.screamingsandals.lib.world.difficulty.DifficultyHolder;
import org.screamingsandals.lib.world.dimension.DimensionHolder;
import org.screamingsandals.lib.world.gamerule.GameRuleHolder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MinestomWorldHolder extends BasicWrapper<Instance> implements WorldHolder {
    public MinestomWorldHolder(Instance wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public UUID getUuid() {
        return wrappedObject.getUniqueId();
    }

    @Override
    public String getName() {
        return wrappedObject.getUniqueId().toString();
    }

    @Override
    public int getMinY() {
        return wrappedObject.getDimensionType().getMinY();
    }

    @Override
    public int getMaxY() {
        return wrappedObject.getDimensionType().getHeight();
    }

    @Override
    public DifficultyHolder getDifficulty() {
        return DifficultyHolder.of(MinecraftServer.getDifficulty());
    }

    @Override
    public DimensionHolder getDimension() {
        return DimensionHolder.of(wrappedObject.getDimensionType());
    }

    @Override
    public Optional<ChunkHolder> getChunkAt(int x, int z) {
        return Optional.empty();
    }

    @Override
    public Optional<ChunkHolder> getChunkAt(LocationHolder location) {
        return Optional.empty();
    }

    @Override
    public List<EntityBasic> getEntities() {
        return null;
    }

    @Override
    public <T> T getGameRuleValue(GameRuleHolder holder) {
        return null;
    }

    @Override
    public <T> void setGameRuleValue(GameRuleHolder holder, T value) {

    }

    @Override
    public long getTime() {
        return wrappedObject.getTime();
    }

    @Override
    public void setTime(long time) {
        wrappedObject.setTime(time);
    }

    @Override
    public void sendParticle(ParticleHolder particle, LocationHolder location) {

    }

    @Override
    public boolean isSpawnKeptInMemory() {
        return false;
    }

    @Override
    public boolean isSpawningOfAnimalsAllowed() {
        return false;
    }

    @Override
    public boolean isSpawningOfMonstersAllowed() {
        return false;
    }

    @Override
    public BlockHolder getHighestBlockAt(int x, int z) {
        return null;
    }

    @Override
    public int getHighestYAt(int x, int z) {
        return 0;
    }

    @Override
    public @NotNull Iterable<? extends Audience> audiences() {
        return wrappedObject.audiences();
    }
}
