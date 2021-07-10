package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.material.slot.EquipmentSlotHolder;
import org.screamingsandals.lib.player.PlayerWrapper;

@EqualsAndHashCode(callSuper = true)
public class SPlayerInteractAtEntityEvent extends SPlayerInteractEntityEvent {

    public SPlayerInteractAtEntityEvent(PlayerWrapper player, EntityBasic clickedEntity, EquipmentSlotHolder hand) {
        super(player, clickedEntity, hand);
    }

}
