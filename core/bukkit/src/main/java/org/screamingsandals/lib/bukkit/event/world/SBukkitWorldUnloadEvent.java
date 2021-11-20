package org.screamingsandals.lib.bukkit.event.world;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.event.world.WorldUnloadEvent;
import org.screamingsandals.lib.bukkit.world.BukkitWorldHolder;
import org.screamingsandals.lib.event.world.SWorldUnloadEvent;
import org.screamingsandals.lib.world.WorldHolder;

@Data
@EqualsAndHashCode(callSuper = true)
public class SBukkitWorldUnloadEvent extends SWorldUnloadEvent {
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
