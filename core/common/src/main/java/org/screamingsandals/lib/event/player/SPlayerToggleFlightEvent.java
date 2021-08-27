package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

@EqualsAndHashCode(callSuper = false)
public class SPlayerToggleFlightEvent extends SPlayerCancellableEvent {
    private final ImmutableObjectLink<Boolean> flying;

    public SPlayerToggleFlightEvent(ImmutableObjectLink<PlayerWrapper> player,
                                    ImmutableObjectLink<Boolean> flying) {
        super(player);
        this.flying = flying;
    }

    public boolean isFlying() {
        return flying.get();
    }
}
