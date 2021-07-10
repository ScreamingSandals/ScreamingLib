package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.world.TimeSkipEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.bukkit.world.BukkitWorldHolder;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.world.STimeSkipEvent;

public class TimeSkipEventListener extends AbstractBukkitEventHandlerFactory<TimeSkipEvent, STimeSkipEvent> {

    public TimeSkipEventListener(Plugin plugin) {
        super(TimeSkipEvent.class, STimeSkipEvent.class, plugin);
    }

    @Override
    protected STimeSkipEvent wrapEvent(TimeSkipEvent event, EventPriority priority) {
        return new STimeSkipEvent(
                new BukkitWorldHolder(event.getWorld()),
                STimeSkipEvent.Reason.valueOf(event.getSkipReason().name()),
                event.getSkipAmount()
        );
    }

    @Override
    protected void postProcess(STimeSkipEvent wrappedEvent, TimeSkipEvent event) {
        event.setSkipAmount(wrappedEvent.getSkipAmount());
    }
}
