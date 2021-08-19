package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.ArrowBodyCountChangeEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SArrowBodyCountChangeEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

public class ArrowBodyCountChangeEventListener extends AbstractBukkitEventHandlerFactory<ArrowBodyCountChangeEvent, SArrowBodyCountChangeEvent> {

    public ArrowBodyCountChangeEventListener(Plugin plugin) {
        super(ArrowBodyCountChangeEvent.class, SArrowBodyCountChangeEvent.class, plugin);
    }

    @Override
    protected SArrowBodyCountChangeEvent wrapEvent(ArrowBodyCountChangeEvent event, EventPriority priority) {
        return new SArrowBodyCountChangeEvent(
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                ImmutableObjectLink.of(event::isReset),
                ImmutableObjectLink.of(event::getOldAmount),
                ObjectLink.of(event::getNewAmount, event::setNewAmount)
        );
    }
}
