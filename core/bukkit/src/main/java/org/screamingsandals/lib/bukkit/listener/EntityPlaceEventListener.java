package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.EntityPlaceEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityPlaceEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.BlockFace;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.world.BlockMapper;

public class EntityPlaceEventListener extends AbstractBukkitEventHandlerFactory<EntityPlaceEvent, SEntityPlaceEvent> {

    public EntityPlaceEventListener(Plugin plugin) {
        super(EntityPlaceEvent.class, SEntityPlaceEvent.class, plugin);
    }

    @Override
    protected SEntityPlaceEvent wrapEvent(EntityPlaceEvent event, EventPriority priority) {
        return new SEntityPlaceEvent(
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getPlayer()).orElseThrow()),
                ImmutableObjectLink.of(() -> BlockMapper.wrapBlock(event.getBlock())),
                ImmutableObjectLink.of(() -> BlockFace.valueOf(event.getBlockFace().name().toUpperCase()))
        );
    }
}
