package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.block.BlockCookEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.world.BlockMapper;
import org.screamingsandals.lib.event.block.SBlockCookEvent;

public class BlockCookEventListener extends AbstractBukkitEventHandlerFactory<BlockCookEvent, SBlockCookEvent> {

    public BlockCookEventListener(Plugin plugin) {
        super(BlockCookEvent.class, SBlockCookEvent.class, plugin);
    }

    @Override
    protected SBlockCookEvent wrapEvent(BlockCookEvent event, EventPriority priority) {
        return new SBlockCookEvent(
                BlockMapper.wrapBlock(event.getBlock()),
                ItemFactory.build(event.getSource()).orElseThrow(),
                ItemFactory.build(event.getResult()).orElseThrow()
        );
    }
}
