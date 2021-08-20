package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.SlimeSplitEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SSlimeSplitEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

public class SlimeSplitEventListener extends AbstractBukkitEventHandlerFactory<SlimeSplitEvent, SSlimeSplitEvent> {

    public SlimeSplitEventListener(Plugin plugin) {
        super(SlimeSplitEvent.class, SSlimeSplitEvent.class, plugin);
    }

    @Override
    protected SSlimeSplitEvent wrapEvent(SlimeSplitEvent event, EventPriority priority) {
        return new SSlimeSplitEvent(
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                ObjectLink.of(event::getCount, event::setCount)
        );
    }
}
