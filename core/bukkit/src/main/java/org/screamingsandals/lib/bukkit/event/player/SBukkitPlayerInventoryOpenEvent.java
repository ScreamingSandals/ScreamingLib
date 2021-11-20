package org.screamingsandals.lib.bukkit.event.player;

import lombok.Data;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.event.player.SPlayerInventoryOpenEvent;
import org.screamingsandals.lib.item.builder.ItemFactory;
import org.screamingsandals.lib.player.PlayerWrapper;

@Data
public class SBukkitPlayerInventoryOpenEvent implements SPlayerInventoryOpenEvent, BukkitCancellable {
    private final InventoryOpenEvent event;

    // Internal cache
    private PlayerWrapper player;
    private Container topInventory;
    private Container bottomInventory;

    @Override
    public PlayerWrapper getPlayer() {
        if (player == null) {
            player = new BukkitEntityPlayer((Player) event.getPlayer());
        }
        return player;
    }

    @Override
    public Container getTopInventory() {
        if (topInventory == null) {
            topInventory = ItemFactory.wrapContainer(event.getInventory()).orElseThrow();
        }
        return topInventory;
    }

    @Override
    public Container getBottomInventory() {
        if (bottomInventory == null) {
            bottomInventory = ItemFactory.wrapContainer(event.getView().getBottomInventory()).orElseThrow();
        }
        return bottomInventory;
    }
}
