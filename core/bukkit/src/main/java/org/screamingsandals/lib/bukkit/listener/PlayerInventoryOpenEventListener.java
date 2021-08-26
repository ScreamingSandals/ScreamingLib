package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.player.SPlayerInventoryOpenEvent;
import org.screamingsandals.lib.item.builder.ItemFactory;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.utils.*;

public class PlayerInventoryOpenEventListener extends AbstractBukkitEventHandlerFactory<InventoryOpenEvent, SPlayerInventoryOpenEvent> {

    public PlayerInventoryOpenEventListener(Plugin plugin) {
        super(InventoryOpenEvent.class, SPlayerInventoryOpenEvent.class, plugin);
    }

    @Override
    protected SPlayerInventoryOpenEvent wrapEvent(InventoryOpenEvent event, EventPriority priority) {
        if (event.getPlayer() instanceof Player) {
            return new SPlayerInventoryOpenEvent(
                    ImmutableObjectLink.of(() -> PlayerMapper.wrapPlayer((Player)event.getPlayer())),
                    ImmutableObjectLink.of(() -> ItemFactory.wrapContainer(event.getInventory()).orElse(null)),
                    ImmutableObjectLink.of(() -> ItemFactory.wrapContainer(event.getView().getBottomInventory()).orElseThrow())
            );
        }

        return null;
    }
}
