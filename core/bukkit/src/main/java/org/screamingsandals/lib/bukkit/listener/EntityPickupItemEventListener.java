package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityItem;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityPickupItemEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.player.SPlayerPickupItemEvent;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

public class EntityPickupItemEventListener extends AbstractBukkitEventHandlerFactory<EntityPickupItemEvent, SEntityPickupItemEvent> {

    public EntityPickupItemEventListener(Plugin plugin) {
        super(EntityPickupItemEvent.class, SEntityPickupItemEvent.class, plugin);
    }

    @Override
    protected SEntityPickupItemEvent wrapEvent(EntityPickupItemEvent event, EventPriority priority) {
        if (event.getEntity() instanceof Player) {
            return new SPlayerPickupItemEvent(
                    ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                    ImmutableObjectLink.of(() -> EntityMapper.<EntityItem>wrapEntity(event.getItem()).orElseThrow()),
                    ImmutableObjectLink.of(event::getRemaining)
            );
        }

        return new SEntityPickupItemEvent(
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                ImmutableObjectLink.of(() -> EntityMapper.<EntityItem>wrapEntity(event.getItem()).orElseThrow()),
                ImmutableObjectLink.of(event::getRemaining)
        );
    }
}
