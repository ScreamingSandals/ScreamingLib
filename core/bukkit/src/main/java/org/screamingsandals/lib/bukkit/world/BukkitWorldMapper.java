package org.screamingsandals.lib.bukkit.world;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.world.WorldHolder;
import org.screamingsandals.lib.world.WorldMapper;

import java.util.Optional;
import java.util.UUID;

@Service
public class BukkitWorldMapper extends WorldMapper {

    public BukkitWorldMapper() {
        converter.registerW2P(World.class, holder -> Bukkit.getWorld(holder.getUuid()));
        converter.registerP2W(World.class, BukkitWorldHolder::new);
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
