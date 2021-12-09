package org.screamingsandals.lib.bukkit.event.world;

import lombok.*;
import org.bukkit.event.world.SpawnChangeEvent;
import org.screamingsandals.lib.bukkit.world.BukkitWorldHolder;
import org.screamingsandals.lib.event.world.SSpawnChangeEvent;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;
import org.screamingsandals.lib.world.WorldHolder;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitSpawnChangeEvent implements SSpawnChangeEvent {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final SpawnChangeEvent event;

    // Internal cache
    private WorldHolder world;
    private LocationHolder oldLocation;
    private LocationHolder newLocation;

    @Override
    public WorldHolder getWorld() {
        if (world == null) {
            world = new BukkitWorldHolder(event.getWorld());
        }
        return world;
    }

    @Override
    public LocationHolder getOldLocation() {
        if (oldLocation == null) {
            oldLocation = LocationMapper.wrapLocation(event.getPreviousLocation());
        }
        return oldLocation;
    }

    @Override
    public LocationHolder getNewLocation() {
        if (newLocation == null) {
            newLocation = LocationMapper.wrapLocation(event.getWorld().getSpawnLocation());
        }
        return newLocation;
    }
}
