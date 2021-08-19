package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.block.MoistureChangeEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.world.BlockMapper;
import org.screamingsandals.lib.event.block.SMoistureChangeEvent;
import org.screamingsandals.lib.world.state.BlockStateMapper;

public class MoistureChangeEventListener extends AbstractBukkitEventHandlerFactory<MoistureChangeEvent, SMoistureChangeEvent> {

    public MoistureChangeEventListener(Plugin plugin) {
        super(MoistureChangeEvent.class, SMoistureChangeEvent.class, plugin);
    }

    @Override
    protected SMoistureChangeEvent wrapEvent(MoistureChangeEvent event, EventPriority priority) {
        return new SMoistureChangeEvent(
                ImmutableObjectLink.of(() -> BlockMapper.wrapBlock(event.getBlock())),
                ImmutableObjectLink.of(() -> BlockStateMapper.wrapBlockState(event.getNewState()).orElseThrow())
        );
    }
}
