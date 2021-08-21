package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.entity.SVehicleCreateEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

public class VehicleCreateEventListener extends AbstractBukkitEventHandlerFactory<VehicleCreateEvent, SVehicleCreateEvent> {

    public VehicleCreateEventListener(Plugin plugin) {
        super(VehicleCreateEvent.class, SVehicleCreateEvent.class, plugin);
    }

    @Override
    protected SVehicleCreateEvent wrapEvent(VehicleCreateEvent event, EventPriority priority) {
        return new SVehicleCreateEvent(ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getVehicle()).orElseThrow()));
    }
}
