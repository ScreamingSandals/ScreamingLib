package org.screamingsandals.lib.event.player;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.world.WorldHolder;

@EqualsAndHashCode(callSuper = false)
@Data
public class SPlayerWorldChangeEvent extends CancellableAbstractEvent implements SPlayerEvent {
    private final ImmutableObjectLink<PlayerWrapper> player;
    private final ImmutableObjectLink<WorldHolder> from;

    public PlayerWrapper getPlayer() {
        return player.get();
    }

    public WorldHolder getFrom() {
        return from.get();
    }
}
