package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.block.BlockReceiveGameEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.event.block.SSculkSensorReceiveEvent;

public class BlockReceiveGameEventListener extends AbstractBukkitEventHandlerFactory<BlockReceiveGameEvent, SSculkSensorReceiveEvent> {

    public BlockReceiveGameEventListener(Plugin plugin) {
        super(BlockReceiveGameEvent.class, SSculkSensorReceiveEvent.class, plugin);
    }

    @Override
    protected SSculkSensorReceiveEvent wrapEvent(BlockReceiveGameEvent event, EventPriority priority) {
        return new SSculkSensorReceiveEvent(
                ImmutableObjectLink.of(() -> BlockMapper.wrapBlock(event.getBlock())),
                ImmutableObjectLink.of(() -> NamespacedMappingKey.of(event.getEvent().getKey().toString())),
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElse(null))
        );
    }
}
