package org.screamingsandals.lib.event.entity;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.slot.EquipmentSlotHolder;

public interface SEntityShootBowEvent extends SCancellableEvent, PlatformEventWrapper {

    EntityBasic getEntity();

    @Nullable
    Item getBow();

    @Nullable
    Item getConsumable();

    EntityBasic getProjectile();

    EquipmentSlotHolder getHand();

    float getForce();

    boolean isConsumeItem();

    void setConsumeItem(boolean consumeItem);
}
