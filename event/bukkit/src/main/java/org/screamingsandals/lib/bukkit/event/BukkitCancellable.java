package org.screamingsandals.lib.bukkit.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public interface BukkitCancellable extends org.screamingsandals.lib.event.Cancellable, NoAutoCancellable {
    <E extends Event & Cancellable> E getEvent();

    @Override
    default boolean isCancelled() {
        return getEvent().isCancelled();
    }

    @Override
    default void setCancelled(boolean cancelled) {
        getEvent().setCancelled(cancelled);
    }
}
