package org.screamingsandals.lib.sponge.world;

import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;
import org.screamingsandals.lib.world.WorldHolder;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.server.ServerLocation;

import java.util.Optional;
import java.util.UUID;

@Service
public class SpongeLocationMapper extends LocationMapper {

    public static void init() {
        LocationMapper.init(SpongeLocationMapper::new);
    }

    public SpongeLocationMapper() {
        // TODO: Somehow handle rotation
        converter.registerW2P(ServerLocation.class, holder -> {
            var world = Sponge.getServer().getWorldManager()
                    .world(ResourceKey.builder()
                            .value(holder.getWorld().getUuid().toString())
                            .build())
                    .orElseThrow();

            return ServerLocation.of(world, holder.getX(), holder.getY(), holder.getZ());
        }).registerP2W(ServerLocation.class, location ->
                new LocationHolder(location.getX(), location.getY(), location.getZ(), 0, 0, new SpongeWorldHolder(location.getWorld()))
        );
    }

    @Override
    protected Optional<WorldHolder> getWorld0(UUID uuid) {
        return Sponge.getServer()
                .getWorldManager()
                .world(ResourceKey.builder()
                        .value(uuid.toString())
                        .build())
                .map(SpongeWorldHolder::new);
    }

    @Override
    protected Optional<WorldHolder> getWorld0(String name) {
        try {
            return getWorld0(UUID.fromString(name));
        } catch (IllegalArgumentException ignored) {
        }
        return Optional.empty(); // TODO
    }
}
