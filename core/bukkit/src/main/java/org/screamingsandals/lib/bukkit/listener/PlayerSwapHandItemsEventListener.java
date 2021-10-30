package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.item.builder.ItemFactory;
import org.screamingsandals.lib.event.player.SPlayerSwapHandItemsEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

public class PlayerSwapHandItemsEventListener extends AbstractBukkitEventHandlerFactory<PlayerSwapHandItemsEvent, SPlayerSwapHandItemsEvent> {

    public PlayerSwapHandItemsEventListener(Plugin plugin) {
        super(PlayerSwapHandItemsEvent.class, SPlayerSwapHandItemsEvent.class, plugin);
    }

    @Override
    protected SPlayerSwapHandItemsEvent wrapEvent(PlayerSwapHandItemsEvent event, EventPriority priority) {
        return new SPlayerSwapHandItemsEvent(
                ImmutableObjectLink.of(() -> new BukkitEntityPlayer(event.getPlayer())),
                ObjectLink.of(
                        () -> ItemFactory.build(event.getMainHandItem()).orElse(null),
                        item -> event.setMainHandItem(item != null ? item.as(ItemStack.class) : null)
                ),
                ObjectLink.of(
                        () -> ItemFactory.build(event.getOffHandItem()).orElse(null),
                        item -> event.setOffHandItem(item != null ? item.as(ItemStack.class) : null)
                )
        );
    }
}
