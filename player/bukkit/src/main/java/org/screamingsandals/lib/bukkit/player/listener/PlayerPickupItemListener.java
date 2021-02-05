package org.screamingsandals.lib.bukkit.player.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.event.SPlayerPickupItemEvent;

public class PlayerPickupItemListener extends AbstractBukkitEventHandlerFactory<EntityPickupItemEvent, SPlayerPickupItemEvent> {

    public PlayerPickupItemListener(Plugin plugin) {
        super(EntityPickupItemEvent.class, SPlayerPickupItemEvent.class, plugin);
    }

    @Override
    protected SPlayerPickupItemEvent wrapEvent(EntityPickupItemEvent event, EventPriority priority) {
        final var entity = event.getEntity();
        if (entity instanceof Player) {
            return new SPlayerPickupItemEvent(
                    PlayerMapper.wrapPlayer((Player) entity),
                    ItemFactory.build(event.getItem().getItemStack()).orElseThrow()
            );
        }
        return null;
    }
}
