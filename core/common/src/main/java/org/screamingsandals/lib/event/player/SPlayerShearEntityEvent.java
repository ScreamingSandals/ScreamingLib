package org.screamingsandals.lib.event.player;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.material.slot.EquipmentSlotHolder;
import org.screamingsandals.lib.player.PlayerWrapper;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class SPlayerShearEntityEvent extends CancellableAbstractEvent {
    private final PlayerWrapper player;
    private final EntityBasic what;
    private final Item item;
    private final EquipmentSlotHolder hand;
}
