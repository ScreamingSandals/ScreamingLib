package org.screamingsandals.lib.bukkit.entity.listener;

import org.bukkit.event.entity.EntityPlaceEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.event.SEntityPlaceEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.BlockFace;
import org.screamingsandals.lib.world.BlockMapper;

@SuppressWarnings("deprecation")
public class EntityPlaceEventListener extends AbstractBukkitEventHandlerFactory<EntityPlaceEvent, SEntityPlaceEvent> {

    public EntityPlaceEventListener(Plugin plugin) {
        super(EntityPlaceEvent.class, SEntityPlaceEvent.class, plugin);
    }

    @Override
    protected SEntityPlaceEvent wrapEvent(EntityPlaceEvent event, EventPriority priority) {
        return new SEntityPlaceEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                EntityMapper.wrapEntity(event.getPlayer()).orElseThrow(),
                BlockMapper.wrapBlock(event.getBlock()),
                BlockFace.valueOf(event.getBlockFace().name().toUpperCase())
        );
    }
}
