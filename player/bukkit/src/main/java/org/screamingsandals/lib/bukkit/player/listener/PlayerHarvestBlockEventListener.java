package org.screamingsandals.lib.bukkit.player.listener;

import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.event.SPlayerHarvestBlockEvent;
import org.screamingsandals.lib.world.BlockMapper;

import java.util.stream.Collectors;

public class PlayerHarvestBlockEventListener extends AbstractBukkitEventHandlerFactory<PlayerHarvestBlockEvent, SPlayerHarvestBlockEvent> {

    public PlayerHarvestBlockEventListener(Plugin plugin) {
        super(PlayerHarvestBlockEvent.class, SPlayerHarvestBlockEvent.class, plugin);
    }

    @Override
    protected SPlayerHarvestBlockEvent wrapEvent(PlayerHarvestBlockEvent event, EventPriority priority) {
        return new SPlayerHarvestBlockEvent(
                PlayerMapper.wrapPlayer(event.getPlayer()),
                BlockMapper.wrapBlock(event.getHarvestedBlock()),
                event.getItemsHarvested()
                        .stream()
                        .map(item -> ItemFactory.build(item).orElse(null))
                        .collect(Collectors.toList())
        );
    }

    @Override
    protected void postProcess(SPlayerHarvestBlockEvent wrappedEvent, PlayerHarvestBlockEvent event) {
        event.getItemsHarvested().clear();
        wrappedEvent
                .getItemsHarvested()
                .stream()
                .map(item -> item.as(ItemStack.class))
                .forEach(event.getItemsHarvested()::add);
    }
}
