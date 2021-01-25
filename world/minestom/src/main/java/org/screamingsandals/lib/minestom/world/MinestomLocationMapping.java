package org.screamingsandals.lib.minestom.world;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.Position;
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.utils.annotations.PlatformMapping;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapping;

import java.util.Objects;

@PlatformMapping(platform = PlatformType.MINESTOM)
public class MinestomLocationMapping extends LocationMapping {

    public static void init() {
        LocationMapping.init(MinestomLocationMapping::new);
    }

    public MinestomLocationMapping() {
        converter
                .registerW2P(InstancedPosition.class, wrapper -> {
                    final var instance = MinecraftServer.getInstanceManager().getInstance(wrapper.getWorldId());
                    if (instance == null) {
                        return null;
                    }

                    return new InstancedPosition(instance, (float) wrapper.getX(), (float) wrapper.getY(), (float) wrapper.getZ(),
                            wrapper.getYaw(), wrapper.getPitch());
                })
                .registerW2P(InstancedBlockPosition.class, wrapper -> {
                    final var instance = MinecraftServer.getInstanceManager().getInstance(wrapper.getWorldId());
                    if (instance == null) {
                        return null;
                    }

                    return new InstancedBlockPosition(instance, wrapper.as(Position.class));
                })
                .registerP2W(InstancedPosition.class, location ->
                        new LocationHolder(location.getX(), location.getY(), location.getZ(),
                                location.getYaw(), location.getPitch(), location.getInstance().getUniqueId()))
                .registerP2W(Player.class, player -> {
                    final var location = player.getPosition();
                    return new LocationHolder(location.getX(), location.getY(), location.getZ(),
                            location.getYaw(), location.getPitch(), Objects.requireNonNull(player.getInstance()).getUniqueId());
                });
    }
}
