package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.block.BlockDispenseArmorEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityLiving;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.item.builder.ItemFactory;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.event.block.SBlockDispenseEvent;

public class BlockDispenseEventListener extends AbstractBukkitEventHandlerFactory<BlockDispenseEvent, SBlockDispenseEvent> {

    public BlockDispenseEventListener(Plugin plugin) {
        super(BlockDispenseEvent.class, SBlockDispenseEvent.class, plugin);
    }

    @Override
    protected SBlockDispenseEvent wrapEvent(BlockDispenseEvent event, EventPriority priority) {
        return new SBlockDispenseEvent(
                ImmutableObjectLink.of(() -> BlockMapper.wrapBlock(event.getBlock())),
                ImmutableObjectLink.of(() -> ItemFactory.build(event.getItem()).orElseThrow()),
                ImmutableObjectLink.of(() -> new Vector3D(event.getVelocity().getX(), event.getVelocity().getY(), event.getVelocity().getZ())),
                ImmutableObjectLink.of(() -> event instanceof BlockDispenseArmorEvent ? EntityMapper.<EntityLiving>wrapEntity(((BlockDispenseArmorEvent) event).getTargetEntity()).orElseThrow() : null)
        );
    }
}
