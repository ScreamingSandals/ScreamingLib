package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

@EqualsAndHashCode(callSuper = false)
public class SPlayerToggleSprintEvent extends SPlayerCancellableEvent {
    private final ImmutableObjectLink<Boolean> sprinting;

    public SPlayerToggleSprintEvent(ImmutableObjectLink<PlayerWrapper> player,
                                    ImmutableObjectLink<Boolean> sprinting) {
        super(player);
        this.sprinting = sprinting;
    }

    public boolean isSprinting() {
        return sprinting.get();
    }
}
