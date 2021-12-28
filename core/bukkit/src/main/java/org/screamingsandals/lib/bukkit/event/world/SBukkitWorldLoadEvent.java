package org.screamingsandals.lib.bukkit.event.world;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bukkit.event.world.WorldLoadEvent;
import org.screamingsandals.lib.event.world.SWorldLoadEvent;
import org.screamingsandals.lib.world.WorldHolder;
import org.screamingsandals.lib.world.WorldMapper;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitWorldLoadEvent implements SWorldLoadEvent {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final WorldLoadEvent event;

    // Internal cache
    private WorldHolder world;

    @Override
    public WorldHolder getWorld() {
        if (world == null) {
            world = WorldMapper.wrapWorld(event.getWorld());
        }
        return world;
    }
}
