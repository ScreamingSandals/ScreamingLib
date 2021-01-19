package org.screamingsandals.bukkit.world;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.screamingsandals.lib.utils.Platform;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapping;

public class BukkitLocationMapping extends LocationMapping {

    public static void init() {
        LocationMapping.init(BukkitLocationMapping::new);
    }

    public BukkitLocationMapping() {
        platform = Platform.JAVA_FLATTENING;

        converter.registerW2P(Location.class, holder -> {
            final var world = Bukkit.getWorld(holder.getWorldId());
            if (world == null) {
                return null;
            }

            return new Location(world, holder.getX(), holder.getY(), holder.getZ(), holder.getYaw(), holder.getPitch());
        }).registerP2W(Location.class, location ->
                new LocationHolder(location.getX(), location.getY(), location.getZ(),
                        location.getYaw(), location.getPitch(), location.getWorld().getUID()));
    }
}
