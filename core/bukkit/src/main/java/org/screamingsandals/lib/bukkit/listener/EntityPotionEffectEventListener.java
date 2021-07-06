package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityPotionEffectEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.meta.PotionEffectMapping;

public class EntityPotionEffectEventListener extends AbstractBukkitEventHandlerFactory<EntityPotionEffectEvent, SEntityPotionEffectEvent> {

    public EntityPotionEffectEventListener(Plugin plugin) {
        super(EntityPotionEffectEvent.class, SEntityPotionEffectEvent.class, plugin);
    }

    @Override
    protected SEntityPotionEffectEvent wrapEvent(EntityPotionEffectEvent event, EventPriority priority) {
        return new SEntityPotionEffectEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                PotionEffectMapping.resolve(event.getOldEffect()).orElseThrow(),
                PotionEffectMapping.resolve(event.getNewEffect()).orElseThrow(),
                SEntityPotionEffectEvent.Cause.valueOf(event.getCause().name().toUpperCase()),
                SEntityPotionEffectEvent.Action.valueOf(event.getAction().name().toUpperCase()),
                event.isOverride()
        );
    }

    @Override
    protected void postProcess(SEntityPotionEffectEvent wrappedEvent, EntityPotionEffectEvent event) {
        event.setOverride(wrappedEvent.isOverride());
    }
}
