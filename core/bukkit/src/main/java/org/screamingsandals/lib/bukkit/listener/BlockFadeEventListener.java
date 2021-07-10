package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.block.*;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.block.SBlockFadeEvent;
import org.screamingsandals.lib.world.BlockMapper;
import org.screamingsandals.lib.world.state.BlockStateMapper;

public class BlockFadeEventListener extends AbstractBukkitEventHandlerFactory<BlockFadeEvent, SBlockFadeEvent> {

    public BlockFadeEventListener(Plugin plugin) {
        super(BlockFadeEvent.class, SBlockFadeEvent.class, plugin);
    }

    @Override
    protected SBlockFadeEvent wrapEvent(BlockFadeEvent event, EventPriority priority) {
        return new SBlockFadeEvent(
                BlockMapper.wrapBlock(event.getBlock()),
                BlockStateMapper.wrapBlockState(event.getNewState()).orElseThrow()
        );
    }
}
