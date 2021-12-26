package org.screamingsandals.lib.bukkit.event.block;

import lombok.*;
import org.bukkit.event.block.BlockCookEvent;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.bukkit.item.BukkitItem;
import org.screamingsandals.lib.event.block.SBlockCookEvent;
import org.screamingsandals.lib.item.Item;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitBlockCookEvent implements SBlockCookEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final BlockCookEvent event;

    // Internal cache
    private BlockHolder block;
    private Item source;

    @Override
    public BlockHolder getBlock() {
        if (block == null) {
            block = BlockMapper.wrapBlock(event.getBlock());
        }
        return block;
    }

    @Override
    public Item getSource() {
        if (source == null) {
            source = new BukkitItem(event.getSource());
        }
        return source;
    }

    @Override
    public Item getResult() {
        return new BukkitItem(event.getResult());
    }

    @Override
    public void setResult(Item item) {
        event.setResult(item.as(ItemStack.class));
    }
}
