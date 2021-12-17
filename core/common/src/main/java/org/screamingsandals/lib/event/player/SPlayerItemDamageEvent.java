package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.item.Item;

public interface SPlayerItemDamageEvent extends SCancellableEvent, SPlayerEvent, PlatformEventWrapper {

    Item getItem();

    int getDamage();

    void setDamage(int damage);
}
