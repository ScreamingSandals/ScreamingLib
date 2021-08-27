package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

@EqualsAndHashCode(callSuper = false)
public class SPlayerEvent extends AbstractEvent {
    private final ImmutableObjectLink<PlayerWrapper> player;

    public SPlayerEvent(ImmutableObjectLink<PlayerWrapper> player) {
        this.player = player;
    }

    public SPlayerEvent(ImmutableObjectLink<PlayerWrapper> player, boolean async) {
        super(async);
        this.player = player;
    }

    public PlayerWrapper getPlayer() {
        return player.get();
    }
}
