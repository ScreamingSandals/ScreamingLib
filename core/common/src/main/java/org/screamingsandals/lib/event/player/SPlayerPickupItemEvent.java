package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityItem;
import org.screamingsandals.lib.event.entity.SEntityPickupItemEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

@EqualsAndHashCode(callSuper = false)
public class SPlayerPickupItemEvent extends SEntityPickupItemEvent {
    public SPlayerPickupItemEvent(ImmutableObjectLink<EntityBasic> entity, ImmutableObjectLink<EntityItem> item, ImmutableObjectLink<Integer> remaining) {
        super(entity, item, remaining);
    }

    public PlayerWrapper getPlayer() {
        return getEntity().as(PlayerWrapper.class);
    }
}
