package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.MaterialMapping;
import org.screamingsandals.lib.world.BlockMapper;
import org.screamingsandals.lib.event.block.SBlockPhysicsEvent;

public class BlockPhysicsEventListener extends AbstractBukkitEventHandlerFactory<BlockPhysicsEvent, SBlockPhysicsEvent> {

    public BlockPhysicsEventListener(Plugin plugin) {
        super(BlockPhysicsEvent.class, SBlockPhysicsEvent.class, plugin);
    }

    @Override
    protected SBlockPhysicsEvent wrapEvent(BlockPhysicsEvent event, EventPriority priority) {
        return new SBlockPhysicsEvent(
                BlockMapper.wrapBlock(event.getBlock()),
                MaterialMapping.resolve(event.getChangedType()).orElseThrow(),
                BlockMapper.wrapBlock(event.getSourceBlock())
        );
    }
}
