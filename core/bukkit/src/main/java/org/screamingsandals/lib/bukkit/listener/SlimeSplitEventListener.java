package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.SlimeSplitEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SSlimeSplitEvent;
import org.screamingsandals.lib.event.EventPriority;

public class SlimeSplitEventListener extends AbstractBukkitEventHandlerFactory<SlimeSplitEvent, SSlimeSplitEvent> {

    public SlimeSplitEventListener(Plugin plugin) {
        super(SlimeSplitEvent.class, SSlimeSplitEvent.class, plugin);
    }

    @Override
    protected SSlimeSplitEvent wrapEvent(SlimeSplitEvent event, EventPriority priority) {
        return new SSlimeSplitEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                event.getCount()
        );
    }

    @Override
    protected void postProcess(SSlimeSplitEvent wrappedEvent, SlimeSplitEvent event) {
        event.setCount(wrappedEvent.getCount());
    }
}
