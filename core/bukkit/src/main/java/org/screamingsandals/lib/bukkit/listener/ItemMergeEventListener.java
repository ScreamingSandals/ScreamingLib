package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityItem;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SItemMergeEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

public class ItemMergeEventListener extends AbstractBukkitEventHandlerFactory<ItemMergeEvent, SItemMergeEvent> {

    public ItemMergeEventListener(Plugin plugin) {
        super(ItemMergeEvent.class, SItemMergeEvent.class, plugin);
    }

    @Override
    protected SItemMergeEvent wrapEvent(ItemMergeEvent event, EventPriority priority) {
        return new SItemMergeEvent(
                ImmutableObjectLink.of(() -> EntityMapper.<EntityItem>wrapEntity(event.getEntity()).orElseThrow()),
                ImmutableObjectLink.of(() -> EntityMapper.<EntityItem>wrapEntity(event.getTarget()).orElseThrow())
        );
    }
}
