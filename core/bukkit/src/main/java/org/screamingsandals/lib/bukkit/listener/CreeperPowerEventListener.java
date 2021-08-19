package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.CreeperPowerEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityLightning;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SCreeperPowerEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

public class CreeperPowerEventListener extends AbstractBukkitEventHandlerFactory<CreeperPowerEvent, SCreeperPowerEvent> {

    public CreeperPowerEventListener(Plugin plugin) {
        super(CreeperPowerEvent.class, SCreeperPowerEvent.class, plugin);
    }

    @Override
    protected SCreeperPowerEvent wrapEvent(CreeperPowerEvent event, EventPriority priority) {
        return new SCreeperPowerEvent(
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                ImmutableObjectLink.of(() -> EntityMapper.<EntityLightning>wrapEntity(event.getLightning()).orElse(null)),
                ImmutableObjectLink.of(() -> SCreeperPowerEvent.PowerCause.valueOf(event.getCause().name().toUpperCase()))
        );
    }
}
