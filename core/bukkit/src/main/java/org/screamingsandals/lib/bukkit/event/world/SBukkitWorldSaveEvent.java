package org.screamingsandals.lib.bukkit.event.world;

import lombok.Data;
import org.bukkit.event.world.WorldSaveEvent;
import org.screamingsandals.lib.bukkit.world.BukkitWorldHolder;
import org.screamingsandals.lib.event.world.SWorldSaveEvent;
import org.screamingsandals.lib.world.WorldHolder;

@Data
public class SBukkitWorldSaveEvent implements SWorldSaveEvent {
    private final WorldSaveEvent event;

    // Internal cache
    private WorldHolder world;

    @Override
    public WorldHolder getWorld() {
        if (world == null) {
            world = new BukkitWorldHolder(event.getWorld());
        }
        return world;
    }
}
