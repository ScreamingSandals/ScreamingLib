package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.world.BlockMapper;
import org.screamingsandals.lib.event.block.SBlockBurnEvent;

public class BlockBurnEventListener extends AbstractBukkitEventHandlerFactory<BlockBurnEvent, SBlockBurnEvent> {

    public BlockBurnEventListener(Plugin plugin) {
        super(BlockBurnEvent.class, SBlockBurnEvent.class, plugin);
    }

    @Override
    protected SBlockBurnEvent wrapEvent(BlockBurnEvent event, EventPriority priority) {
        return new SBlockBurnEvent(
                ImmutableObjectLink.of(() -> BlockMapper.wrapBlock(event.getBlock())),
                ImmutableObjectLink.of(() -> BlockMapper.resolve(event.getIgnitingBlock()).orElse(null))
        );
    }
}
