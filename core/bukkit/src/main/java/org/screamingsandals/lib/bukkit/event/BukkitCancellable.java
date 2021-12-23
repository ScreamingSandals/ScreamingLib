package org.screamingsandals.lib.bukkit.event;

import org.bukkit.event.Cancellable;

public interface BukkitCancellable extends org.screamingsandals.lib.event.Cancellable, NoAutoCancellable {
    Cancellable getEvent();

    @Override
    default boolean isCancelled() {
        return getEvent().isCancelled();
    }

    @Override
    default void setCancelled(boolean cancelled) {
        getEvent().setCancelled(cancelled);
    }
}
