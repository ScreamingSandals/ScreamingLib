package org.screamingsandals.lib.event.entity;

import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.item.Item;

import java.util.Collection;

public interface SEntityDeathEvent extends SCancellableEvent, PlatformEventWrapper {
    Collection<Item> getDrops();

    EntityBasic getEntity();

    int getDropExp();

    void setDropExp(int dropExp);
}
