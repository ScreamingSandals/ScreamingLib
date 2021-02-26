package org.screamingsandals.lib.bukkit.player.listener;

import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.event.SPlayerPickupItemEvent;

@SuppressWarnings("deprecation") //legacy bukkit versions
public class LegacyPlayerPickupItemListener extends AbstractBukkitEventHandlerFactory<PlayerPickupItemEvent, SPlayerPickupItemEvent> {

    public LegacyPlayerPickupItemListener(Plugin plugin) {
        super(PlayerPickupItemEvent.class, SPlayerPickupItemEvent.class, plugin);
    }

    @Override
    protected SPlayerPickupItemEvent wrapEvent(PlayerPickupItemEvent event, EventPriority priority) {
        return new SPlayerPickupItemEvent(
                PlayerMapper.wrapPlayer(event.getPlayer()),
                ItemFactory.build(event.getItem().getItemStack()).orElseThrow()
        );
    }
}
