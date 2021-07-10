package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerSwapHandItemsEvent;

public class PlayerSwapHandItemsEventListener extends AbstractBukkitEventHandlerFactory<PlayerSwapHandItemsEvent, SPlayerSwapHandItemsEvent> {

    public PlayerSwapHandItemsEventListener(Plugin plugin) {
        super(PlayerSwapHandItemsEvent.class, SPlayerSwapHandItemsEvent.class, plugin);
    }

    @Override
    protected SPlayerSwapHandItemsEvent wrapEvent(PlayerSwapHandItemsEvent event, EventPriority priority) {
        return new SPlayerSwapHandItemsEvent(
                PlayerMapper.wrapPlayer(event.getPlayer()),
                ItemFactory.build(event.getMainHandItem()).orElse(null),
                ItemFactory.build(event.getOffHandItem()).orElse(null)
        );
    }

    @Override
    protected void postProcess(SPlayerSwapHandItemsEvent wrappedEvent, PlayerSwapHandItemsEvent event) {
        event.setMainHandItem(wrappedEvent.getMainHandItem().as(ItemStack.class));
        event.setOffHandItem(wrappedEvent.getOffHandItem().as(ItemStack.class));
    }
}
