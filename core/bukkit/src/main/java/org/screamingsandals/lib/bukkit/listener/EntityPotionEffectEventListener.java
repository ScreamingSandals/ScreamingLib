package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityPotionEffectEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.item.meta.PotionEffectHolder;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

public class EntityPotionEffectEventListener extends AbstractBukkitEventHandlerFactory<EntityPotionEffectEvent, SEntityPotionEffectEvent> {

    public EntityPotionEffectEventListener(Plugin plugin) {
        super(EntityPotionEffectEvent.class, SEntityPotionEffectEvent.class, plugin);
    }

    @Override
    protected SEntityPotionEffectEvent wrapEvent(EntityPotionEffectEvent event, EventPriority priority) {
        return new SEntityPotionEffectEvent(
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                ImmutableObjectLink.of(() -> PotionEffectHolder.of(event.getOldEffect())),
                ImmutableObjectLink.of(() -> PotionEffectHolder.of(event.getNewEffect())),
                ImmutableObjectLink.of(() -> SEntityPotionEffectEvent.Cause.valueOf(event.getCause().name().toUpperCase())),
                ImmutableObjectLink.of(() -> SEntityPotionEffectEvent.Action.valueOf(event.getAction().name().toUpperCase())),
                ObjectLink.of(event::isOverride, event::setOverride)
        );
    }
}
