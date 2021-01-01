package org.screamingsandals.lib.utils.event;

public interface Cancellable {
    boolean isCancelled();

    void setCancelled(boolean cancelled);
}
