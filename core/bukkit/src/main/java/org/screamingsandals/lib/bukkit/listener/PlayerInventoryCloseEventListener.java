package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.player.SPlayerInventoryCloseEvent;
import org.screamingsandals.lib.item.builder.ItemFactory;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

public class PlayerInventoryCloseEventListener extends AbstractBukkitEventHandlerFactory<InventoryCloseEvent, SPlayerInventoryCloseEvent> {

    public PlayerInventoryCloseEventListener(Plugin plugin) {
        super(InventoryCloseEvent.class, SPlayerInventoryCloseEvent.class, plugin);
    }

    @Override
    protected SPlayerInventoryCloseEvent wrapEvent(InventoryCloseEvent event, EventPriority priority) {
        if (event.getPlayer() instanceof Player) {
            return new SPlayerInventoryCloseEvent(
                    ImmutableObjectLink.of(() -> new BukkitEntityPlayer((Player)event.getPlayer())),
                    ImmutableObjectLink.of(() -> ItemFactory.wrapContainer(event.getInventory()).orElse(null)),
                    ImmutableObjectLink.of(() -> ItemFactory.wrapContainer(event.getView().getBottomInventory()).orElseThrow()),
                    ImmutableObjectLink.of(() -> NamespacedMappingKey.of(event.getReason().name()))
            );
        }

        return null;
    }
}
