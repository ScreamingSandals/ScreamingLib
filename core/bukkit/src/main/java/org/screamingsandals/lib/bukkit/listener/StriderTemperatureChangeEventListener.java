package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.StriderTemperatureChangeEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityLiving;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SStriderTemperatureChangeEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

public class StriderTemperatureChangeEventListener extends AbstractBukkitEventHandlerFactory<StriderTemperatureChangeEvent, SStriderTemperatureChangeEvent> {

    public StriderTemperatureChangeEventListener(Plugin plugin) {
        super(StriderTemperatureChangeEvent.class, SStriderTemperatureChangeEvent.class, plugin);
    }

    @Override
    protected SStriderTemperatureChangeEvent wrapEvent(StriderTemperatureChangeEvent event, EventPriority priority) {
        return new SStriderTemperatureChangeEvent(
                ImmutableObjectLink.of(() -> EntityMapper.<EntityLiving>wrapEntity(event.getEntity()).orElseThrow()),
                ImmutableObjectLink.of(event::isShivering)
        );
    }
}
