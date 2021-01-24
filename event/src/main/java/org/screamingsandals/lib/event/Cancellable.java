package org.screamingsandals.lib.event;

public interface Cancellable {
    boolean isCancelled();

    void setCancelled(boolean cancelled);
}
