package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.item.builder.ItemFactory;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerInventoryClickEvent;
import org.screamingsandals.lib.utils.*;

public class PlayerInventoryClickEventListener extends AbstractBukkitEventHandlerFactory<InventoryClickEvent, SPlayerInventoryClickEvent> {

    public PlayerInventoryClickEventListener(Plugin plugin) {
        super(InventoryClickEvent.class, SPlayerInventoryClickEvent.class, plugin);
    }

    @Override
    protected SPlayerInventoryClickEvent wrapEvent(InventoryClickEvent event, EventPriority priority) {
        if (event.getWhoClicked() instanceof Player) {
            return new SPlayerInventoryClickEvent(
                    ImmutableObjectLink.of(() -> PlayerMapper.wrapPlayer((Player)event.getWhoClicked())),
                    ObjectLink.of(() -> ItemFactory.build(event.getCursor()).orElse(null), null),
                    ObjectLink.of(() -> ItemFactory.build(event.getCurrentItem()).orElse(null), null),
                    ImmutableObjectLink.of(() -> ItemFactory.wrapContainer(event.getClickedInventory()).orElse(null)),
                    ImmutableObjectLink.of(() -> ClickType.convert(event.getClick().name())),
                    ImmutableObjectLink.of(() -> ItemFactory.wrapContainer(event.getInventory()).orElseThrow()),
                    ImmutableObjectLink.of(() -> InventoryAction.convert(event.getAction().name())),
                    ImmutableObjectLink.of(event::getHotbarButton),
                    ImmutableObjectLink.of(event::getSlot),
                    ImmutableObjectLink.of(() -> SlotType.convert(event.getSlotType().name())),
                    ImmutableObjectLink.of(event::getRawSlot),
                    ObjectLink.of(
                            () -> AbstractEvent.Result.convert(event.getResult().name()),
                            result -> event.setResult(Event.Result.valueOf(result.name()))
                    )
            );
        }

        return null;
    }
}
