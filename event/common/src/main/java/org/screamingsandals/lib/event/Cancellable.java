package org.screamingsandals.lib.event;

public interface Cancellable {
    /**
     * Indicates if the event was cancelled
     *
     * @return true if the event was cancelled
     */
    boolean isCancelled();

    /**
     * Sets new state
     *
     * @param cancelled to cancel
     */
    void setCancelled(boolean cancelled);
}
