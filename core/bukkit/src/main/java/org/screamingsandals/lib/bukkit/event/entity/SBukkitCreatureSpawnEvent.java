package org.screamingsandals.lib.bukkit.event.entity;

import org.bukkit.event.entity.CreatureSpawnEvent;
import org.screamingsandals.lib.event.entity.SCreatureSpawnEvent;

public class SBukkitCreatureSpawnEvent extends SBukkitEntitySpawnEvent implements SCreatureSpawnEvent {
    public SBukkitCreatureSpawnEvent(CreatureSpawnEvent event) {
        super(event);
    }

    // Internal cache
    private SpawnReason spawnReason;

    @Override
    public SpawnReason getSpawnReason() {
        if (spawnReason == null) {
            spawnReason = SpawnReason.valueOf(((CreatureSpawnEvent) getEvent()).getSpawnReason().name());
        }
        return spawnReason;
    }
}
