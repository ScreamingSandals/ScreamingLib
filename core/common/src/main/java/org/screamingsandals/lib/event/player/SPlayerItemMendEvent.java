package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.entity.EntityExperience;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.item.Item;

public interface SPlayerItemMendEvent extends SCancellableEvent, SPlayerEvent {

    Item getItem();

    EntityExperience getExperienceOrb();

    int getRepairAmount();

    void setRepairAmount(int repairAmount);
}
