package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.slot.EquipmentSlotHolder;

public interface SPlayerShearEntityEvent extends SCancellableEvent, SPlayerEvent {

    EntityBasic getWhat();

    Item getItem();

    EquipmentSlotHolder getHand();
}
