package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SExplosionPrimeEvent;
import org.screamingsandals.lib.event.EventPriority;

public class ExplosionPrimeEventListener extends AbstractBukkitEventHandlerFactory<ExplosionPrimeEvent, SExplosionPrimeEvent> {

    public ExplosionPrimeEventListener(Plugin plugin) {
        super(ExplosionPrimeEvent.class, SExplosionPrimeEvent.class, plugin);
    }

    @Override
    protected SExplosionPrimeEvent wrapEvent(ExplosionPrimeEvent event, EventPriority priority) {
        return new SExplosionPrimeEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                event.getRadius(),
                event.getFire()
        );
    }

    @Override
    protected void postProcess(SExplosionPrimeEvent wrappedEvent, ExplosionPrimeEvent event) {
        event.setFire(wrappedEvent.isFire());
        event.setRadius(wrappedEvent.getRadius());
    }
}
