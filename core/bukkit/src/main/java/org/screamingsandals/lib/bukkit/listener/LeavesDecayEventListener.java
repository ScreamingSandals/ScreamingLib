package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.world.BlockMapper;
import org.screamingsandals.lib.event.block.SLeavesDecayEvent;

public class LeavesDecayEventListener extends AbstractBukkitEventHandlerFactory<LeavesDecayEvent, SLeavesDecayEvent> {

    public LeavesDecayEventListener(Plugin plugin) {
        super(LeavesDecayEvent.class, SLeavesDecayEvent.class, plugin);
    }

    @Override
    protected SLeavesDecayEvent wrapEvent(LeavesDecayEvent event, EventPriority priority) {
        return new SLeavesDecayEvent(
                BlockMapper.wrapBlock(event.getBlock())
        );
    }
}
