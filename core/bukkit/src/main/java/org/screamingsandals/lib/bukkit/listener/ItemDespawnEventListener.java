package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SItemDespawnEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.world.LocationMapper;

public class ItemDespawnEventListener extends AbstractBukkitEventHandlerFactory<ItemDespawnEvent, SItemDespawnEvent> {

    public ItemDespawnEventListener(Plugin plugin) {
        super(ItemDespawnEvent.class, SItemDespawnEvent.class, plugin);
    }

    @Override
    protected SItemDespawnEvent wrapEvent(ItemDespawnEvent event, EventPriority priority) {
        return new SItemDespawnEvent(
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                ImmutableObjectLink.of(() -> LocationMapper.wrapLocation(event.getLocation()))
        );
    }
}
