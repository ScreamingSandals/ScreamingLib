package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityExplodeEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.CollectionLinkedToCollection;
import org.screamingsandals.lib.world.BlockMapper;
import org.screamingsandals.lib.world.LocationMapper;

public class EntityExplodeEventListener extends AbstractBukkitEventHandlerFactory<EntityExplodeEvent, SEntityExplodeEvent> {

    public EntityExplodeEventListener(Plugin plugin) {
        super(EntityExplodeEvent.class, SEntityExplodeEvent.class, plugin);
    }

    @Override
    protected SEntityExplodeEvent wrapEvent(EntityExplodeEvent event, EventPriority priority) {
        return new SEntityExplodeEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                LocationMapper.wrapLocation(event.getLocation()),
                new CollectionLinkedToCollection<>(event.blockList(), o -> o.as(Block.class), BlockMapper::wrapBlock), // because it's mutable
                event.getYield()
        );
    }

    @Override
    protected void postProcess(SEntityExplodeEvent wrappedEvent, EntityExplodeEvent event) {
        event.setYield(wrappedEvent.getYield());

    }
}
