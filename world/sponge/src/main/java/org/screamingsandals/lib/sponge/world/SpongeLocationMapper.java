package org.screamingsandals.lib.sponge.world;

import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.server.ServerLocation;

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
                            .value(holder.getWorldId().toString())
                            .build())
                    .orElseThrow();

            return ServerLocation.of(world, holder.getX(), holder.getY(), holder.getZ());
        }).registerP2W(ServerLocation.class, location ->
                new LocationHolder(location.getX(), location.getY(), location.getZ(), 0, 0, location.getWorld().getUniqueId())
        );
    }
}
