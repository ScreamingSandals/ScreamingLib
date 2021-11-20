package org.screamingsandals.lib.bukkit.event.world;

import lombok.Data;
import org.bukkit.event.world.WorldInitEvent;
import org.screamingsandals.lib.bukkit.world.BukkitWorldHolder;
import org.screamingsandals.lib.event.world.SWorldInitEvent;
import org.screamingsandals.lib.world.WorldHolder;

@Data
public class SBukkitWorldInitEvent implements SWorldInitEvent {
    private final WorldInitEvent event;

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
