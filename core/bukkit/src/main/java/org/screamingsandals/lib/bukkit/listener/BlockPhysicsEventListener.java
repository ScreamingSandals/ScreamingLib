package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.MaterialMapping;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.world.BlockMapper;
import org.screamingsandals.lib.event.block.SBlockPhysicsEvent;

public class BlockPhysicsEventListener extends AbstractBukkitEventHandlerFactory<BlockPhysicsEvent, SBlockPhysicsEvent> {

    public BlockPhysicsEventListener(Plugin plugin) {
        super(BlockPhysicsEvent.class, SBlockPhysicsEvent.class, plugin);
    }

    @Override
    protected SBlockPhysicsEvent wrapEvent(BlockPhysicsEvent event, EventPriority priority) {
        return new SBlockPhysicsEvent(
                ImmutableObjectLink.of(() -> BlockMapper.wrapBlock(event.getBlock())),
                ImmutableObjectLink.of(() -> MaterialMapping.resolve(event.getChangedType()).orElseThrow()),
                ImmutableObjectLink.of(() -> BlockMapper.wrapBlock(event.getSourceBlock()))
        );
    }
}
