package org.screamingsandals.lib.bukkit.entity.listener;

import org.bukkit.event.entity.EntityBreakDoorEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.event.SEntityBreakDoorEvent;
import org.screamingsandals.lib.entity.event.SEntityChangeBlockEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.world.BlockDataMapper;
import org.screamingsandals.lib.world.BlockMapper;

public class EntityChangeBlockEventListener extends AbstractBukkitEventHandlerFactory<EntityChangeBlockEvent, SEntityChangeBlockEvent> {

    public EntityChangeBlockEventListener(Plugin plugin) {
        super(EntityChangeBlockEvent.class, SEntityChangeBlockEvent.class, plugin);
    }

    @Override
    protected SEntityChangeBlockEvent wrapEvent(EntityChangeBlockEvent event, EventPriority priority) {
        if (event instanceof EntityBreakDoorEvent) {
            return new SEntityBreakDoorEvent(
                    EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                    BlockMapper.wrapBlock(event.getBlock()),
                    BlockDataMapper.resolve(event.getTo()).orElseThrow()
            );
        }
        return new SEntityChangeBlockEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                BlockMapper.wrapBlock(event.getBlock()),
                BlockDataMapper.resolve(event.getBlockData()).orElseThrow()
        );
    }
}
