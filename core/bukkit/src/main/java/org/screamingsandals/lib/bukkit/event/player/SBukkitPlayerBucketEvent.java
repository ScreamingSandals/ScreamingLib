package org.screamingsandals.lib.bukkit.event.player;

import lombok.*;
import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.player.SPlayerBucketEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.ItemTypeHolder;
import org.screamingsandals.lib.item.builder.ItemFactory;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.BlockFace;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitPlayerBucketEvent implements SPlayerBucketEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final PlayerBucketEvent event;

    // Internal cache
    private PlayerWrapper player;
    private BlockHolder block;
    private BlockHolder blockClicked;
    private BlockFace blockFace;
    private ItemTypeHolder bucket;

    @Override
    public PlayerWrapper getPlayer() {
        if (player == null) {
            player = new BukkitEntityPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public BlockHolder getBlock() {
        if (block == null) {
            block = BlockMapper.wrapBlock(event.getBlock());
        }
        return block;
    }

    @Override
    public BlockHolder getBlockClicked() {
        if (blockClicked == null) {
            blockClicked = BlockMapper.wrapBlock(event.getBlockClicked());
        }
        return blockClicked;
    }

    @Override
    public BlockFace getBlockFace() {
        if (blockFace == null) {
            blockFace = BlockFace.valueOf(event.getBlockFace().name());
        }
        return blockFace;
    }

    @Override
    public ItemTypeHolder getBucket() {
        if (bucket == null) {
            bucket = ItemTypeHolder.of(event.getBucket());
        }
        return bucket;
    }

    @Override
    @Nullable
    public Item getItem() {
        return event.getItemStack() != null ? ItemFactory.build(event.getItemStack()).orElseThrow() : null;
    }

    @Override
    public void setItem(@Nullable Item item) {
        event.setItemStack(item == null ? null : item.as(ItemStack.class));
    }

    @Override
    public Action getAction() {
        return  event instanceof PlayerBucketFillEvent ? Action.FILL : Action.EMPTY;
    }
}
