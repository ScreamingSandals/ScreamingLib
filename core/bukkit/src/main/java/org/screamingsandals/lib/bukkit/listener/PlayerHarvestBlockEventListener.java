package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.item.builder.ItemFactory;
import org.screamingsandals.lib.event.player.SPlayerHarvestBlockEvent;
import org.screamingsandals.lib.utils.CollectionLinkedToCollection;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.block.BlockMapper;

public class PlayerHarvestBlockEventListener extends AbstractBukkitEventHandlerFactory<PlayerHarvestBlockEvent, SPlayerHarvestBlockEvent> {

    public PlayerHarvestBlockEventListener(Plugin plugin) {
        super(PlayerHarvestBlockEvent.class, SPlayerHarvestBlockEvent.class, plugin);
    }

    @Override
    protected SPlayerHarvestBlockEvent wrapEvent(PlayerHarvestBlockEvent event, EventPriority priority) {
        return new SPlayerHarvestBlockEvent(
                ImmutableObjectLink.of(() -> new BukkitEntityPlayer(event.getPlayer())),
                ImmutableObjectLink.of(() -> BlockMapper.wrapBlock(event.getHarvestedBlock())),
                new CollectionLinkedToCollection<>(
                        event.getItemsHarvested(),
                        item -> item.as(ItemStack.class),
                        itemStack -> ItemFactory.build(itemStack).orElse(null)
                )
        );
    }
}
