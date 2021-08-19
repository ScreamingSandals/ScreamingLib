package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.EntityBreakDoorEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityBreakDoorEvent;
import org.screamingsandals.lib.event.entity.SEntityChangeBlockEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
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
                    ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                    ImmutableObjectLink.of(() -> BlockMapper.wrapBlock(event.getBlock())),
                    ImmutableObjectLink.of(() -> {
                        try {
                            return BlockDataMapper.resolve(event.getBlockData()).orElseThrow();
                        } catch (Throwable ignored) {
                            return BlockDataMapper.resolve(event.getTo().getNewData((byte) 0)).orElseThrow();
                        }
                    })
            );
        }
        return new SEntityChangeBlockEvent(
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                ImmutableObjectLink.of(() -> BlockMapper.wrapBlock(event.getBlock())),
                ImmutableObjectLink.of(() -> {
                    try {
                        return BlockDataMapper.resolve(event.getBlockData()).orElseThrow();
                    } catch (Throwable ignored) {
                        return BlockDataMapper.resolve(event.getTo().getNewData((byte) 0)).orElseThrow();
                    }
                })
        );
    }
}
