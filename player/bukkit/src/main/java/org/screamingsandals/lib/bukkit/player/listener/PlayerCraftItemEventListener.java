package org.screamingsandals.lib.bukkit.player.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.event.SPlayerCraftItemEvent;
import org.screamingsandals.lib.utils.ClickType;
import org.screamingsandals.lib.utils.InventoryAction;
import org.screamingsandals.lib.utils.InventoryType;

public class PlayerCraftItemEventListener extends AbstractBukkitEventHandlerFactory<CraftItemEvent, SPlayerCraftItemEvent> {

    public PlayerCraftItemEventListener(Plugin plugin) {
        super(CraftItemEvent.class, SPlayerCraftItemEvent.class, plugin);
    }

    @Override
    protected SPlayerCraftItemEvent wrapEvent(CraftItemEvent event, EventPriority priority) {
        if (event.getWhoClicked() instanceof Player) {
            return new SPlayerCraftItemEvent(
                    PlayerMapper.wrapPlayer((Player)event.getWhoClicked()),
                    ItemFactory.build(event.getCurrentItem()).orElseThrow(),
                    ItemFactory.wrapContainer(event.getClickedInventory()).orElse(null),
                    ItemFactory.wrapContainer(event.getInventory()).orElseThrow(),
                    ClickType.convert(event.getClick().name()),
                    new SPlayerCraftItemEvent.Recipe(
                            ItemFactory.build(event.getRecipe().getResult()).orElseThrow()
                    ),
                    AbstractEvent.Result.convert(event.getResult().name()),
                    InventoryAction.convert(event.getAction().name()),
                    ItemFactory.build(event.getCursor()).orElse(null),
                    InventoryType.SlotType.convert(event.getSlotType().name()),
                    event.getHotbarButton(),
                    event.getRawSlot()
            );
        }

        return null;
    }

    @Override
    protected void postProcess(SPlayerCraftItemEvent wrappedEvent, CraftItemEvent event) {
        event.setCurrentItem(wrappedEvent.getCurrentItem().as(ItemStack.class));
        event.setResult(Event.Result.valueOf(wrappedEvent.getResult().name().toUpperCase()));
    }
}
