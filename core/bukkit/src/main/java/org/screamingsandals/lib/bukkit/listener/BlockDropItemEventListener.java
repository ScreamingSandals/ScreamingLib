package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.entity.Item;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityItem;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.block.SBlockDropItemEvent;
import org.screamingsandals.lib.utils.CollectionLinkedToCollection;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.block.state.BlockStateMapper;

public class BlockDropItemEventListener extends AbstractBukkitEventHandlerFactory<BlockDropItemEvent, SBlockDropItemEvent> {

    public BlockDropItemEventListener(Plugin plugin) {
        super(BlockDropItemEvent.class, SBlockDropItemEvent.class, plugin);
    }

    @Override
    protected SBlockDropItemEvent wrapEvent(BlockDropItemEvent event, EventPriority priority) {
        return new SBlockDropItemEvent(
                ImmutableObjectLink.of(() -> PlayerMapper.wrapPlayer(event.getPlayer())),
                ImmutableObjectLink.of(() -> BlockStateMapper.wrapBlockState(event.getBlockState()).orElseThrow()),
                new CollectionLinkedToCollection<>(event.getItems(), entityItem -> entityItem.as(Item.class), item -> EntityMapper.<EntityItem>wrapEntity(item).orElseThrow())
        );
    }
}
