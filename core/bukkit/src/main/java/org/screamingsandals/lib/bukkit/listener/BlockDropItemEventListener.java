package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.block.SBlockDropItemEvent;
import org.screamingsandals.lib.world.state.BlockStateMapper;

import java.util.List;

public class BlockDropItemEventListener extends AbstractBukkitEventHandlerFactory<BlockDropItemEvent, SBlockDropItemEvent> {

    public BlockDropItemEventListener(Plugin plugin) {
        super(BlockDropItemEvent.class, SBlockDropItemEvent.class, plugin);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected SBlockDropItemEvent wrapEvent(BlockDropItemEvent event, EventPriority priority) {
        return new SBlockDropItemEvent(
                PlayerMapper.wrapPlayer(event.getPlayer()),
                BlockStateMapper.wrapBlockState(event.getBlockState()).orElseThrow(),
                ItemFactory.buildAll((List) event.getItems())
        );
    }
}
