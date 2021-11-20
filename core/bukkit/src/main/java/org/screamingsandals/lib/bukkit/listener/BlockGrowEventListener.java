package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.bukkit.event.block.SBukkitBlockFormEvent;
import org.screamingsandals.lib.bukkit.event.block.SBukkitBlockFormedByEntityEvent;
import org.screamingsandals.lib.bukkit.event.block.SBukkitBlockGrowEvent;
import org.screamingsandals.lib.bukkit.event.block.SBukkitBlockSpreadEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.block.SBlockGrowEvent;

public class BlockGrowEventListener extends AbstractBukkitEventHandlerFactory<BlockGrowEvent, SBlockGrowEvent> {

    public BlockGrowEventListener(Plugin plugin) {
        super(BlockGrowEvent.class, SBlockGrowEvent.class, plugin);
    }

    @Override
    protected SBlockGrowEvent wrapEvent(BlockGrowEvent event, EventPriority priority) {
        if (event instanceof BlockFormEvent) {
            if (event instanceof EntityBlockFormEvent) {
                return new SBukkitBlockFormedByEntityEvent((EntityBlockFormEvent) event);
            }

            if (event instanceof BlockSpreadEvent) {
                return new SBukkitBlockSpreadEvent((BlockSpreadEvent) event);
            }

            return new SBukkitBlockFormEvent((BlockFormEvent) event);
        }

        return new SBukkitBlockGrowEvent(event);
    }
}
