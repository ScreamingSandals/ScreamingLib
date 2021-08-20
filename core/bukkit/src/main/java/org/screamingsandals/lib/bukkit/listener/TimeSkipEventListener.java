package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.world.TimeSkipEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.bukkit.world.BukkitWorldHolder;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.world.STimeSkipEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

public class TimeSkipEventListener extends AbstractBukkitEventHandlerFactory<TimeSkipEvent, STimeSkipEvent> {

    public TimeSkipEventListener(Plugin plugin) {
        super(TimeSkipEvent.class, STimeSkipEvent.class, plugin);
    }

    @Override
    protected STimeSkipEvent wrapEvent(TimeSkipEvent event, EventPriority priority) {
        return new STimeSkipEvent(
                ImmutableObjectLink.of(() -> new BukkitWorldHolder(event.getWorld())),
                ImmutableObjectLink.of(() -> STimeSkipEvent.Reason.valueOf(event.getSkipReason().name())),
                ObjectLink.of(event::getSkipAmount, event::setSkipAmount)
        );
    }
}
