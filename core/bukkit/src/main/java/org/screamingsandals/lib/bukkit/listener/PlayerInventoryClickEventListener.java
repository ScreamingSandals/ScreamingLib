package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerInventoryClickEvent;
import org.screamingsandals.lib.utils.ClickType;
import org.screamingsandals.lib.utils.InventoryAction;
import org.screamingsandals.lib.utils.SlotType;

//TODO:
public class PlayerInventoryClickEventListener extends AbstractBukkitEventHandlerFactory<InventoryClickEvent, SPlayerInventoryClickEvent> {

    public PlayerInventoryClickEventListener(Plugin plugin) {
        super(InventoryClickEvent.class, SPlayerInventoryClickEvent.class, plugin);
    }

    @Override
    protected SPlayerInventoryClickEvent wrapEvent(InventoryClickEvent event, EventPriority priority) {
        if (event.getWhoClicked() instanceof Player) {
            return new SPlayerInventoryClickEvent(
                    PlayerMapper.wrapPlayer((Player)event.getWhoClicked()),
                    ItemFactory.build(event.getCursor()).orElse(null),
                    ItemFactory.build(event.getCurrentItem()).orElse(null),
                    ItemFactory.wrapContainer(event.getClickedInventory()).orElse(null),
                    ClickType.convert(event.getClick().name()),
                    ItemFactory.wrapContainer(event.getInventory()).orElseThrow(),
                    InventoryAction.convert(event.getAction().name()),
                    event.getHotbarButton(),
                    event.getSlot(),
                    SlotType.convert(event.getSlotType().name()),
                    event.getRawSlot(),
                    AbstractEvent.Result.convert(event.getResult().name())
            );
        }

        return null;
    }

    @Override
    protected void postProcess(SPlayerInventoryClickEvent wrappedEvent, InventoryClickEvent event) {
        event.setResult(Event.Result.valueOf(wrappedEvent.getResult().name().toUpperCase()));
        final var currentItem = wrappedEvent.getCurrentItem();
        if (currentItem != null) {
            event.setCurrentItem(wrappedEvent.getCurrentItem().as(ItemStack.class));
        } else {
            event.setCurrentItem(null);
        }
    }
}
