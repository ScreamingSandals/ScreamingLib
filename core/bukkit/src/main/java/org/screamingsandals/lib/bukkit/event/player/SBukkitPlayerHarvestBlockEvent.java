package org.screamingsandals.lib.bukkit.event.player;

import lombok.*;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.bukkit.item.BukkitItem;
import org.screamingsandals.lib.event.player.SPlayerHarvestBlockEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.CollectionLinkedToCollection;

import java.util.Collection;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitPlayerHarvestBlockEvent implements SPlayerHarvestBlockEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final PlayerHarvestBlockEvent event;

    // Internal cache
    private PlayerWrapper player;
    private Collection<Item> itemsHarvested;
    private BlockHolder harvestedBlock;

    @Override
    public PlayerWrapper getPlayer() {
        if (player == null) {
            player = new BukkitEntityPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public Collection<Item> getItemsHarvested() {
        if (itemsHarvested == null) {
            itemsHarvested = new CollectionLinkedToCollection<>(
                    event.getItemsHarvested(),
                    item -> item.as(ItemStack.class),
                    BukkitItem::new
            );
        }
        return itemsHarvested;
    }

    @Override
    public BlockHolder getHarvestedBlock() {
        if (harvestedBlock == null) {
            harvestedBlock = BlockMapper.wrapBlock(event.getHarvestedBlock());
        }
        return harvestedBlock;
    }
}
