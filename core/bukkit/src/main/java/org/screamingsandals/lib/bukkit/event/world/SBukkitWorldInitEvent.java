package org.screamingsandals.lib.bukkit.event.world;

import lombok.*;
import org.bukkit.event.world.WorldInitEvent;
import org.screamingsandals.lib.bukkit.world.BukkitWorldHolder;
import org.screamingsandals.lib.event.world.SWorldInitEvent;
import org.screamingsandals.lib.world.WorldHolder;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitWorldInitEvent implements SWorldInitEvent {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
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
