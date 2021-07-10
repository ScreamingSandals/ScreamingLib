package org.screamingsandals.lib.event.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.material.slot.EquipmentSlotHolder;
import org.screamingsandals.lib.player.PlayerWrapper;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SPlayerInteractEntityEvent extends CancellableAbstractEvent {
    private final PlayerWrapper player;
    private EntityBasic clickedEntity;
    private EquipmentSlotHolder hand;
}
