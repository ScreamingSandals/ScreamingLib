package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.entity.SProjectileLaunchEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

@EqualsAndHashCode(callSuper = false)
public class SPlayerProjectileLaunchEvent extends SProjectileLaunchEvent {
    private final ImmutableObjectLink<PlayerWrapper> player;

    public SPlayerProjectileLaunchEvent(ImmutableObjectLink<PlayerWrapper> player, ImmutableObjectLink<EntityBasic> projectile) {
        super(projectile);
        this.player = player;
    }

    public PlayerWrapper getPlayer() {
        return player.get();
    }
}
