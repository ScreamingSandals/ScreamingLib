package org.screamingsandals.lib.bukkit.event.world;

import lombok.Data;
import org.bukkit.event.world.WorldUnloadEvent;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.bukkit.world.BukkitWorldHolder;
import org.screamingsandals.lib.event.world.SWorldUnloadEvent;
import org.screamingsandals.lib.world.WorldHolder;

@Data
public class SBukkitWorldUnloadEvent implements SWorldUnloadEvent, BukkitCancellable {
    private final WorldUnloadEvent event;

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
