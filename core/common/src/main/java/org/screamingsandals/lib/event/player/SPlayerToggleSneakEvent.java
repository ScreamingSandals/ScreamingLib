package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

@EqualsAndHashCode(callSuper = false)
public class SPlayerToggleSneakEvent extends SPlayerCancellableEvent {
    private final ImmutableObjectLink<Boolean> sneaking;

    public SPlayerToggleSneakEvent(ImmutableObjectLink<PlayerWrapper> player,
                                   ImmutableObjectLink<Boolean> sneaking) {
        super(player);
        this.sneaking = sneaking;
    }

    public boolean isSneaking() {
        return sneaking.get();
    }
}
