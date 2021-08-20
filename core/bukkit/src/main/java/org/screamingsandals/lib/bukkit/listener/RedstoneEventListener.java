package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.world.BlockMapper;
import org.screamingsandals.lib.event.block.SRedstoneEvent;

public class RedstoneEventListener extends AbstractBukkitEventHandlerFactory<BlockRedstoneEvent, SRedstoneEvent> {

    public RedstoneEventListener(Plugin plugin) {
        super(BlockRedstoneEvent.class, SRedstoneEvent.class, plugin);
    }

    @Override
    protected SRedstoneEvent wrapEvent(BlockRedstoneEvent event, EventPriority priority) {
        return new SRedstoneEvent(
                ImmutableObjectLink.of(() -> BlockMapper.wrapBlock(event.getBlock())),
                ImmutableObjectLink.of(event::getOldCurrent),
                ObjectLink.of(event::getNewCurrent, event::setNewCurrent)
        );
    }
}
