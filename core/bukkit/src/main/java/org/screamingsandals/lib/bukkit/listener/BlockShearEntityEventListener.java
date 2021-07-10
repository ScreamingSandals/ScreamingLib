package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.block.BlockShearEntityEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.world.BlockMapper;
import org.screamingsandals.lib.event.block.SBlockShearEntityEvent;

public class BlockShearEntityEventListener extends AbstractBukkitEventHandlerFactory<BlockShearEntityEvent, SBlockShearEntityEvent> {

    public BlockShearEntityEventListener(Plugin plugin) {
        super(BlockShearEntityEvent.class, SBlockShearEntityEvent.class, plugin);
    }

    @Override
    protected SBlockShearEntityEvent wrapEvent(BlockShearEntityEvent event, EventPriority priority) {
        return new SBlockShearEntityEvent(
                BlockMapper.wrapBlock(event.getBlock()),
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                ItemFactory.build(event.getTool()).orElseThrow()
        );
    }
}
