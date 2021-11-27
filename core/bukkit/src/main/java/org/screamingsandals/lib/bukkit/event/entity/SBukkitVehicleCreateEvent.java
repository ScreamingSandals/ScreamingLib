package org.screamingsandals.lib.bukkit.event.entity;

import lombok.Data;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SVehicleCreateEvent;

@Data
public class SBukkitVehicleCreateEvent implements SVehicleCreateEvent, BukkitCancellable {
    private final VehicleCreateEvent event;

    // Internal cache
    private EntityBasic entity;

    @Override
    public EntityBasic getEntity() {
        if (entity == null) {
            entity = EntityMapper.wrapEntity(event.getVehicle()).orElseThrow();
        }
        return entity;
    }
}
