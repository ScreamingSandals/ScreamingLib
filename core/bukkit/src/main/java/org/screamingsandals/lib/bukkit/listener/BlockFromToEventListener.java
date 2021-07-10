package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.BlockFace;
import org.screamingsandals.lib.world.BlockMapper;
import org.screamingsandals.lib.event.block.SBlockFromToEvent;

public class BlockFromToEventListener extends AbstractBukkitEventHandlerFactory<BlockFromToEvent, SBlockFromToEvent> {

    public BlockFromToEventListener(Plugin plugin) {
        super(BlockFromToEvent.class, SBlockFromToEvent.class, plugin);
    }

    @Override
    protected SBlockFromToEvent wrapEvent(BlockFromToEvent event, EventPriority priority) {
        return new SBlockFromToEvent(
                BlockMapper.wrapBlock(event.getBlock()),
                BlockMapper.wrapBlock(event.getToBlock()),
                BlockFace.valueOf(event.getFace().name())
        );
    }
}
