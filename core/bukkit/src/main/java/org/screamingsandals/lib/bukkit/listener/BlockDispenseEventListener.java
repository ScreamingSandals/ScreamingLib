package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.block.BlockDispenseArmorEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityLiving;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.world.BlockMapper;
import org.screamingsandals.lib.event.block.SBlockDispenseEvent;

public class BlockDispenseEventListener extends AbstractBukkitEventHandlerFactory<BlockDispenseEvent, SBlockDispenseEvent> {

    public BlockDispenseEventListener(Plugin plugin) {
        super(BlockDispenseEvent.class, SBlockDispenseEvent.class, plugin);
    }

    @Override
    protected SBlockDispenseEvent wrapEvent(BlockDispenseEvent event, EventPriority priority) {
        return new SBlockDispenseEvent(
                BlockMapper.wrapBlock(event.getBlock()),
                ItemFactory.build(event.getItem()).orElseThrow(),
                new Vector3D(event.getVelocity().getX(), event.getVelocity().getY(), event.getVelocity().getZ()),
                event instanceof BlockDispenseArmorEvent ? EntityMapper.<EntityLiving>wrapEntity(((BlockDispenseArmorEvent) event).getTargetEntity()).orElseThrow() : null
        );
    }
}
