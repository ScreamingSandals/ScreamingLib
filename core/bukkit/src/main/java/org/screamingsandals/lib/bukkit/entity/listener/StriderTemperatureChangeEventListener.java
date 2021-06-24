package org.screamingsandals.lib.bukkit.entity.listener;

import org.bukkit.event.entity.StriderTemperatureChangeEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.event.SStriderTemperatureChangeEvent;
import org.screamingsandals.lib.event.EventPriority;

public class StriderTemperatureChangeEventListener extends AbstractBukkitEventHandlerFactory<StriderTemperatureChangeEvent, SStriderTemperatureChangeEvent> {

    public StriderTemperatureChangeEventListener(Plugin plugin) {
        super(StriderTemperatureChangeEvent.class, SStriderTemperatureChangeEvent.class, plugin);
    }

    @Override
    protected SStriderTemperatureChangeEvent wrapEvent(StriderTemperatureChangeEvent event, EventPriority priority) {
        return new SStriderTemperatureChangeEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                event.isShivering()
        );
    }
}
