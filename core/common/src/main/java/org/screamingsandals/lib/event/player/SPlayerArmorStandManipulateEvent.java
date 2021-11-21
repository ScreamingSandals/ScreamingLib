package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.slot.EquipmentSlotHolder;

public interface SPlayerArmorStandManipulateEvent extends SPlayerInteractEntityEvent {

    Item getPlayerItem();

    Item getArmorStandItem();

    EquipmentSlotHolder getSlot();
}
