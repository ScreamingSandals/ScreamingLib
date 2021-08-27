package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.world.WorldHolder;

@EqualsAndHashCode(callSuper = false)
public class SPlayerWorldChangeEvent extends SPlayerCancellableEvent {
    private final ImmutableObjectLink<WorldHolder> from;

    public SPlayerWorldChangeEvent(ImmutableObjectLink<PlayerWrapper> player,
                                   ImmutableObjectLink<WorldHolder> from) {
        super(player);
        this.from = from;
    }

    public WorldHolder getFrom() {
        return from.get();
    }
}
