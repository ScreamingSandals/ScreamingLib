package org.screamingsandals.lib.sponge.world;

import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapping;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.server.ServerLocation;

public class SpongeLocationMapping extends LocationMapping {

    public static void init() {
        LocationMapping.init(SpongeLocationMapping::new);
    }

    public SpongeLocationMapping() {
        // TODO: Somehow handle rotation
        converter.registerW2P(ServerLocation.class, holder -> {
            var world = Sponge.getServer().getWorldManager().worldKey(holder.getWorldId())
                    .flatMap(resourceKey -> Sponge.getServer().getWorldManager().world(resourceKey))
                    .orElseThrow();
            return ServerLocation.of(world, holder.getX(), holder.getY(), holder.getZ());
        }).registerP2W(ServerLocation.class, location ->
                new LocationHolder(location.getX(), location.getY(), location.getZ(), 0, 0, location.getWorld().getUniqueId())
        );
    }
}
