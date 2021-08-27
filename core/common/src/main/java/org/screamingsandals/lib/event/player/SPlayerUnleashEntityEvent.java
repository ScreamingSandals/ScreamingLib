package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.entity.SEntityUnleashEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

@EqualsAndHashCode(callSuper = false)
public class SPlayerUnleashEntityEvent extends SEntityUnleashEvent implements SPlayerEvent {
    private final ImmutableObjectLink<PlayerWrapper> player;

    public SPlayerUnleashEntityEvent(ImmutableObjectLink<EntityBasic> entity, ImmutableObjectLink<UnleashReason> reason, ImmutableObjectLink<PlayerWrapper> player) {
        super(entity, reason);
        this.player = player;
    }

    public PlayerWrapper getPlayer() {
        return player.get();
    }
}
