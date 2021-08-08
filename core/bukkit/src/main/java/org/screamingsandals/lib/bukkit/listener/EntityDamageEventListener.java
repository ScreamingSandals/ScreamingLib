package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.damage.DamageCauseHolder;
import org.screamingsandals.lib.event.entity.SEntityDamageByBlockEvent;
import org.screamingsandals.lib.event.entity.SEntityDamageByEntityEvent;
import org.screamingsandals.lib.event.entity.SEntityDamageEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.world.BlockMapper;

public class EntityDamageEventListener extends AbstractBukkitEventHandlerFactory<EntityDamageEvent, SEntityDamageEvent> {

    public EntityDamageEventListener(Plugin plugin) {
        super(EntityDamageEvent.class, SEntityDamageEvent.class, plugin);
    }

    @Override
    protected SEntityDamageEvent wrapEvent(EntityDamageEvent event, EventPriority priority) {
        if (event instanceof EntityDamageByEntityEvent) {
            return new SEntityDamageByEntityEvent(
                    EntityMapper.wrapEntity(((EntityDamageByEntityEvent)event).getDamager()).orElseThrow(),
                    EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                    DamageCauseHolder.of(event.getCause()),
                    event.getDamage()
            );
        }

        if (event instanceof EntityDamageByBlockEvent) {
            return new SEntityDamageByBlockEvent(
                    BlockMapper.wrapBlock(((EntityDamageByBlockEvent)event).getDamager()),
                    EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                    DamageCauseHolder.of(event.getCause()),
                    event.getDamage()
            );
        }

        return new SEntityDamageEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                DamageCauseHolder.of(event.getCause()),
                event.getDamage()
        );
    }

    @Override
    protected void postProcess(SEntityDamageEvent wrappedEvent, EntityDamageEvent event) {
        event.setDamage(wrappedEvent.getDamage());
    }
}
