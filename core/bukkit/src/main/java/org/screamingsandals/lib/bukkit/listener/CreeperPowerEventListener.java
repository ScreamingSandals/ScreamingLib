package org.screamingsandals.lib.bukkit.listener;
import org.bukkit.event.entity.CreeperPowerEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityLightning;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SCreeperPowerEvent;
import org.screamingsandals.lib.event.EventPriority;

public class CreeperPowerEventListener extends AbstractBukkitEventHandlerFactory<CreeperPowerEvent, SCreeperPowerEvent> {

    public CreeperPowerEventListener(Plugin plugin) {
        super(CreeperPowerEvent.class, SCreeperPowerEvent.class, plugin);
    }

    @Override
    protected SCreeperPowerEvent wrapEvent(CreeperPowerEvent event, EventPriority priority) {
        return new SCreeperPowerEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                EntityMapper.<EntityLightning>wrapEntity(event.getLightning()).orElseThrow(),
                SCreeperPowerEvent.PowerCause.valueOf(event.getCause().name().toUpperCase())
        );
    }
}
