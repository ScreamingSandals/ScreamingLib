package org.screamingsandals.lib.bukkit.entity.listener;
import org.bukkit.event.entity.CreeperPowerEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.LightningStrike;
import org.screamingsandals.lib.entity.event.SCreeperPowerEvent;
import org.screamingsandals.lib.event.EventPriority;

public class CreeperPowerEventListener extends AbstractBukkitEventHandlerFactory<CreeperPowerEvent, SCreeperPowerEvent> {

    public CreeperPowerEventListener(Plugin plugin) {
        super(CreeperPowerEvent.class, SCreeperPowerEvent.class, plugin);
    }

    @Override
    protected SCreeperPowerEvent wrapEvent(CreeperPowerEvent event, EventPriority priority) {
        var lighting = event.getLightning();

        return new SCreeperPowerEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                new LightningStrike(
                        EntityMapper.wrapEntity(lighting).orElse(null),
                        lighting != null && lighting.isEffect()
                ),
                SCreeperPowerEvent.PowerCause.valueOf(event.getCause().name().toUpperCase())
        );
    }
}
