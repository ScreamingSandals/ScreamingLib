package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.slot.EquipmentSlotHolder;

public interface SPlayerInteractEntityEvent extends SCancellableEvent, SPlayerEvent, PlatformEventWrapper {

    EntityBasic getClickedEntity();

    EquipmentSlotHolder getHand();
}
