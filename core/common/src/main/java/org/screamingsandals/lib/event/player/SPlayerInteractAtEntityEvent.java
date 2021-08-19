package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.material.slot.EquipmentSlotHolder;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

@EqualsAndHashCode(callSuper = true)
public class SPlayerInteractAtEntityEvent extends SPlayerInteractEntityEvent {

    public SPlayerInteractAtEntityEvent(ImmutableObjectLink<PlayerWrapper> player, ImmutableObjectLink<EntityBasic> clickedEntity, ImmutableObjectLink<EquipmentSlotHolder> hand) {
        super(player, clickedEntity, hand);
    }

}
