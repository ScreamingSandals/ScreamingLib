package org.screamingsandals.lib.minestom.world;

import net.kyori.adventure.audience.Audience;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.particle.Particle;
import net.minestom.server.particle.ParticleCreator;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.particle.ParticleHolder;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.WorldHolder;
import org.screamingsandals.lib.world.chunk.ChunkHolder;
import org.screamingsandals.lib.world.chunk.ChunkMapper;
import org.screamingsandals.lib.world.difficulty.DifficultyHolder;
import org.screamingsandals.lib.world.dimension.DimensionHolder;
import org.screamingsandals.lib.world.gamerule.GameRuleHolder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        return ChunkMapper.wrapChunk(wrappedObject.getChunkAt(x, z));
    }

    @Override
    public Optional<ChunkHolder> getChunkAt(LocationHolder location) {
        return ChunkMapper.wrapChunk(wrappedObject.getChunkAt(location.getX(), location.getZ()));
    }

    @Override
    public List<EntityBasic> getEntities() {
        return wrappedObject.getEntities().stream()
                .map(EntityMapper::wrapEntity)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public <T> T getGameRuleValue(GameRuleHolder holder) {
        throw new UnsupportedOperationException("Minestom does not support game rules");
    }

    @Override
    public <T> void setGameRuleValue(GameRuleHolder holder, T value) {
        throw new UnsupportedOperationException("Minestom does not support game rules");
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
        final var minestomParticle = ParticleCreator.createParticlePacket(
                particle.particleType().as(Particle.class),
                particle.longDistance(),
                location.getX(),
                location.getY(),
                location.getZ(),
                (float) particle.offset().getX(),
                (float) particle.offset().getY(),
                (float) particle.offset().getZ(),
                (float) particle.particleData(),
                particle.count(),
                null
        );
        for (Player player : wrappedObject.getPlayers()) {
            if (player.getPosition().distance(location.as(Pos.class)) <= ((particle.longDistance()) ? 512D : 32D)) {
                //noinspection UnstableApiUsage
                player.getPlayerConnection().sendPacket(minestomParticle);
            }
        }
    }

    @Override
    public boolean isSpawnKeptInMemory() {
        // TODO: maybe hook into vanilla reimpl and check this better
        return wrappedObject.getChunkAt(0D, 0D) != null;
    }

    @Override
    public boolean isSpawningOfAnimalsAllowed() {
        return true;
    }

    @Override
    public boolean isSpawningOfMonstersAllowed() {
        return true;
    }

    @Override
    public BlockHolder getHighestBlockAt(int x, int z) {
        return BlockMapper.wrapBlock(wrappedObject.getBlock(x, getHighestYAt(x, z), z));
    }

    @Override
    public int getHighestYAt(int x, int z) {
        return IntStream.rangeClosed(wrappedObject.getDimensionType().getMinY(), wrappedObject.getDimensionType().getHeight())
                .filter(y -> !wrappedObject.getBlock(x, y, z).isAir())
                .max()
                .orElse(wrappedObject.getDimensionType().getMinY());
    }

    @Override
    public @NotNull Iterable<? extends Audience> audiences() {
        return wrappedObject.audiences();
    }
}
