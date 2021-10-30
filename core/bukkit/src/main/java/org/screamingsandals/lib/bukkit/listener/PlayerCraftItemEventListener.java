package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.item.builder.ItemFactory;
import org.screamingsandals.lib.event.player.SPlayerCraftItemEvent;
import org.screamingsandals.lib.utils.*;

public class PlayerCraftItemEventListener extends AbstractBukkitEventHandlerFactory<CraftItemEvent, SPlayerCraftItemEvent> {

    public PlayerCraftItemEventListener(Plugin plugin) {
        super(CraftItemEvent.class, SPlayerCraftItemEvent.class, plugin);
    }

    @Override
    protected SPlayerCraftItemEvent wrapEvent(CraftItemEvent event, EventPriority priority) {
        if (event.getWhoClicked() instanceof Player) {
            return new SPlayerCraftItemEvent(
                    ImmutableObjectLink.of(() -> new BukkitEntityPlayer((Player) event.getWhoClicked())),
                    ObjectLink.of(
                            () -> ItemFactory.build(event.getCurrentItem()).orElseThrow(),
                            item -> event.setCurrentItem(item.as(ItemStack.class))
                    ),
                    ImmutableObjectLink.of(() -> ItemFactory.wrapContainer(event.getClickedInventory()).orElse(null)),
                    ImmutableObjectLink.of(() -> ItemFactory.wrapContainer(event.getInventory()).orElseThrow()),
                    ImmutableObjectLink.of(() -> ClickType.convert(event.getClick().name())),
                    ImmutableObjectLink.of(() -> new SPlayerCraftItemEvent.Recipe(
                            ItemFactory.build(event.getRecipe().getResult()).orElseThrow()
                    )),
                    ObjectLink.of(
                            () -> AbstractEvent.Result.convert(event.getResult().name()),
                            result -> Event.Result.valueOf(result.name().toUpperCase())
                    ),
                    ImmutableObjectLink.of(() -> InventoryAction.convert(event.getAction().name())),
                    ImmutableObjectLink.of(() -> ItemFactory.build(event.getCursor()).orElse(null)),
                    ImmutableObjectLink.of(() -> SlotType.convert(event.getSlotType().name())),
                    ImmutableObjectLink.of(event::getHotbarButton),
                    ImmutableObjectLink.of(event::getRawSlot)
            );
        }

        return null;
    }
}
