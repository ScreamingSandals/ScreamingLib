package org.screamingsandals.lib.bukkit.world;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;
import org.screamingsandals.lib.world.WorldHolder;

import java.util.Optional;
import java.util.UUID;

@Service
public class BukkitLocationMapper extends LocationMapper {

    public BukkitLocationMapper() {
        converter.registerW2P(Location.class, holder -> {
            final var world = Bukkit.getWorld(holder.getWorld().getUuid());
            if (world == null) {
                return null;
            }
            return new Location(world, holder.getX(), holder.getY(), holder.getZ(), holder.getYaw(), holder.getPitch());
        }).registerP2W(Location.class, location ->
                new LocationHolder(location.getX(), location.getY(), location.getZ(),
                        location.getYaw(), location.getPitch(), new BukkitWorldHolder(location.getWorld())));
    }

    @Override
    protected Optional<WorldHolder> getWorld0(UUID uuid) {
        return Optional.ofNullable(Bukkit.getWorld(uuid)).map(BukkitWorldHolder::new);
    }

    @Override
    protected Optional<WorldHolder> getWorld0(String name) {
        try {
            return getWorld0(UUID.fromString(name));
        } catch (IllegalArgumentException ignored) {
        }
        return Optional.ofNullable(Bukkit.getWorld(name)).map(BukkitWorldHolder::new);
    }
}
