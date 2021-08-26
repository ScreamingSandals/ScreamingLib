package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.block.BlockShearEntityEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.item.builder.ItemFactory;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.event.block.SBlockShearEntityEvent;

public class BlockShearEntityEventListener extends AbstractBukkitEventHandlerFactory<BlockShearEntityEvent, SBlockShearEntityEvent> {

    public BlockShearEntityEventListener(Plugin plugin) {
        super(BlockShearEntityEvent.class, SBlockShearEntityEvent.class, plugin);
    }

    @Override
    protected SBlockShearEntityEvent wrapEvent(BlockShearEntityEvent event, EventPriority priority) {
        return new SBlockShearEntityEvent(
                ImmutableObjectLink.of(() -> BlockMapper.wrapBlock(event.getBlock())),
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                ImmutableObjectLink.of(() -> ItemFactory.build(event.getTool()).orElseThrow())
        );
    }
}
