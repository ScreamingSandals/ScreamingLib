package org.screamingsandals.lib.minestom.world;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.Position;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;
import org.screamingsandals.lib.world.WorldHolder;

import java.util.Optional;
import java.util.UUID;

@Service
public class MinestomLocationMapper extends LocationMapper {

    public static void init() {
        LocationMapper.init(MinestomLocationMapper::new);
    }

    public MinestomLocationMapper() {
        converter
                .registerW2P(InstancedPosition.class, wrapper -> {
                    final var instance = MinecraftServer.getInstanceManager().getInstance(wrapper.getWorld().getUuid());
                    if (instance == null) {
                        return null;
                    }

                    return new InstancedPosition(instance, (float) wrapper.getX(), (float) wrapper.getY(), (float) wrapper.getZ(),
                            wrapper.getYaw(), wrapper.getPitch());
                })
                .registerW2P(InstancedBlockPosition.class, wrapper -> {
                    final var instance = MinecraftServer.getInstanceManager().getInstance(wrapper.getWorld().getUuid());
                    if (instance == null) {
                        return null;
                    }

                    return new InstancedBlockPosition(instance, wrapper.as(Position.class));
                })
                .registerP2W(InstancedPosition.class, location ->
                        new LocationHolder(location.getX(), location.getY(), location.getZ(),
                                location.getYaw(), location.getPitch(), new MinestomWorldHolder(location.getInstance())))
                .registerP2W(Player.class, player -> {
                    final var location = player.getPosition();
                    return new LocationHolder(location.getX(), location.getY(), location.getZ(),
                            location.getYaw(), location.getPitch(), new MinestomWorldHolder(player.getInstance()));
                });
    }

    @Override
    protected Optional<WorldHolder> getWorld0(UUID uuid) {
        return Optional.ofNullable(MinecraftServer.getInstanceManager().getInstance(uuid)).map(MinestomWorldHolder::new);
    }

    @Override
    protected Optional<WorldHolder> getWorld0(String name) {
        try {
            return getWorld0(UUID.fromString(name));
        } catch (IllegalArgumentException ignored) {
        }
        return Optional.empty(); // probably there's no name in Minestom
    }
}
