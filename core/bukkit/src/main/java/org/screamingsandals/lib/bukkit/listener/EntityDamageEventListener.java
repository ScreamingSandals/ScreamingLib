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
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.world.BlockMapper;

public class EntityDamageEventListener extends AbstractBukkitEventHandlerFactory<EntityDamageEvent, SEntityDamageEvent> {

    public EntityDamageEventListener(Plugin plugin) {
        super(EntityDamageEvent.class, SEntityDamageEvent.class, plugin);
    }

    @Override
    protected SEntityDamageEvent wrapEvent(EntityDamageEvent event, EventPriority priority) {
        if (event instanceof EntityDamageByEntityEvent) {
            return new SEntityDamageByEntityEvent(
                    ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(((EntityDamageByEntityEvent)event).getDamager()).orElseThrow()),
                    ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                    ImmutableObjectLink.of(() -> DamageCauseHolder.of(event.getCause())),
                    ObjectLink.of(event::getDamage, event::setDamage)
            );
        }

        if (event instanceof EntityDamageByBlockEvent) {
            return new SEntityDamageByBlockEvent(
                    ImmutableObjectLink.of(() -> BlockMapper.resolve(((EntityDamageByBlockEvent)event).getDamager()).orElse(null)),
                    ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                    ImmutableObjectLink.of(() -> DamageCauseHolder.of(event.getCause())),
                    ObjectLink.of(event::getDamage, event::setDamage)
            );
        }

        return new SEntityDamageEvent(
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                ImmutableObjectLink.of(() -> DamageCauseHolder.of(event.getCause())),
                ObjectLink.of(event::getDamage, event::setDamage)
        );
    }
}
