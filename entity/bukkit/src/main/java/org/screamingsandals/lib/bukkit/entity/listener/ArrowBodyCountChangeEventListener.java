package org.screamingsandals.lib.bukkit.entity.listener;

import org.bukkit.event.entity.ArrowBodyCountChangeEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.event.SArrowBodyCountChangeEvent;
import org.screamingsandals.lib.event.EventPriority;

public class ArrowBodyCountChangeEventListener extends AbstractBukkitEventHandlerFactory<ArrowBodyCountChangeEvent, SArrowBodyCountChangeEvent> {

    public ArrowBodyCountChangeEventListener(Plugin plugin) {
        super(ArrowBodyCountChangeEvent.class, SArrowBodyCountChangeEvent.class, plugin);
    }

    @Override
    protected SArrowBodyCountChangeEvent wrapEvent(ArrowBodyCountChangeEvent event, EventPriority priority) {
        return new SArrowBodyCountChangeEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                event.isReset(),
                event.getOldAmount(),
                event.getNewAmount()
        );
    }
}
