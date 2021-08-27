package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.event.Cancellable;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

public class SPlayerCancellableEvent extends SPlayerEvent implements Cancellable {
    private boolean cancelled = false;

    public SPlayerCancellableEvent(ImmutableObjectLink<PlayerWrapper> player, boolean async) {
        super(player, async);
    }

    public SPlayerCancellableEvent(ImmutableObjectLink<PlayerWrapper> player) {
        super(player);
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
