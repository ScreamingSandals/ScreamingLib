package org.screamingsandals.lib.bukkit.event.block;

import lombok.Data;
import org.bukkit.entity.Item;
import org.bukkit.event.block.BlockDropItemEvent;
import org.screamingsandals.lib.block.state.BlockStateHolder;
import org.screamingsandals.lib.block.state.BlockStateMapper;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityItem;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.block.SBlockDropItemEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.CollectionLinkedToCollection;

import java.util.Collection;

@Data
public class SBukkitBlockDropItemEvent implements SBlockDropItemEvent, BukkitCancellable {
    private final BlockDropItemEvent event;

    // Internal cache
    private PlayerWrapper player;
    private BlockStateHolder blockState;
    private Collection<EntityItem> items;

    @Override
    public PlayerWrapper getPlayer() {
        if (player == null) {
            player = new BukkitEntityPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public BlockStateHolder getBlockState() {
        if (blockState == null) {
            blockState = BlockStateMapper.wrapBlockState(event.getBlockState()).orElseThrow();
        }
        return blockState;
    }

    @Override
    public Collection<EntityItem> getItems() {
        if (items == null) {
            items = new CollectionLinkedToCollection<>(event.getItems(), entityItem -> entityItem.as(Item.class), item -> EntityMapper.<EntityItem>wrapEntity(item).orElseThrow());
        }
        return items;
    }
}
