package org.screamingsandals.lib.bukkit.event.world;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.event.world.WorldLoadEvent;
import org.screamingsandals.lib.bukkit.world.BukkitWorldHolder;
import org.screamingsandals.lib.event.world.SWorldLoadEvent;
import org.screamingsandals.lib.world.WorldHolder;

@EqualsAndHashCode(callSuper = true)
@Data
public class SBukkitWorldLoadEvent extends SWorldLoadEvent {
    private final WorldLoadEvent event;

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
