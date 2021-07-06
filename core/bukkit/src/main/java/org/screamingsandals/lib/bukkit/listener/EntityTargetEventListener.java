package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityTargetEvent;
import org.screamingsandals.lib.event.entity.SEntityTargetLivingEntityEvent;
import org.screamingsandals.lib.event.EventPriority;

public class EntityTargetEventListener extends AbstractBukkitEventHandlerFactory<EntityTargetEvent, SEntityTargetEvent> {

    public EntityTargetEventListener(Plugin plugin) {
        super(EntityTargetEvent.class, SEntityTargetEvent.class, plugin);
    }

    @Override
    protected SEntityTargetEvent wrapEvent(EntityTargetEvent event, EventPriority priority) {
        if (event instanceof EntityTargetLivingEntityEvent) {
            return new SEntityTargetLivingEntityEvent(
                    EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                    EntityMapper.wrapEntity(event.getTarget()).orElseThrow(),
                    SEntityTargetEvent.TargetReason.valueOf(event.getReason().name().toUpperCase())
            );
        }
        return new SEntityTargetEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                EntityMapper.wrapEntity(event.getTarget()).orElseThrow(),
                SEntityTargetEvent.TargetReason.valueOf(event.getReason().name().toUpperCase())
        );
    }
}
