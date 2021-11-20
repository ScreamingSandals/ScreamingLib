package org.screamingsandals.lib.bukkit.event.world;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.event.world.WorldInitEvent;
import org.screamingsandals.lib.bukkit.world.BukkitWorldHolder;
import org.screamingsandals.lib.event.world.SWorldInitEvent;
import org.screamingsandals.lib.world.WorldHolder;

@Data
@EqualsAndHashCode(callSuper = true)
public class SBukkitWorldInitEvent extends SWorldInitEvent {
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
