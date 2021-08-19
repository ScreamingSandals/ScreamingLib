package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.BatToggleSleepEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SBatToggleSleepEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

public class BatToggleSleepEventListener extends AbstractBukkitEventHandlerFactory<BatToggleSleepEvent, SBatToggleSleepEvent> {

    public BatToggleSleepEventListener(Plugin plugin) {
        super(BatToggleSleepEvent.class, SBatToggleSleepEvent.class, plugin);
    }

    @Override
    protected SBatToggleSleepEvent wrapEvent(BatToggleSleepEvent event, EventPriority priority) {
        return new SBatToggleSleepEvent(
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                ImmutableObjectLink.of(event::isAwake)
        );
    }
}
