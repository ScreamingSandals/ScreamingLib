package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.block.*;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.bukkit.event.block.SBukkitBlockPistonEvent;
import org.screamingsandals.lib.bukkit.event.block.SBukkitBlockPistonExtendEvent;
import org.screamingsandals.lib.bukkit.event.block.SBukkitBlockPistonRetractEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.block.SBlockPistonEvent;

public class BlockPistonEventListener extends AbstractBukkitEventHandlerFactory<BlockPistonEvent, SBlockPistonEvent> {

    public BlockPistonEventListener(Plugin plugin) {
        super(BlockPistonEvent.class, SBlockPistonEvent.class, plugin);
    }

    @Override
    protected SBlockPistonEvent wrapEvent(BlockPistonEvent event, EventPriority priority) {
        if (event instanceof BlockPistonExtendEvent) {
            return new SBukkitBlockPistonExtendEvent((BlockPistonExtendEvent) event);
        }

        if (event instanceof BlockPistonRetractEvent) {
            return new SBukkitBlockPistonRetractEvent((BlockPistonRetractEvent) event);
        }

        return new SBukkitBlockPistonEvent(event);
    }
}
