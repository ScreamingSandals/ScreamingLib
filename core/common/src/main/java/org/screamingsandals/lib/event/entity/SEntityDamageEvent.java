package org.screamingsandals.lib.event.entity;

import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.damage.DamageCauseHolder;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;

public interface SEntityDamageEvent extends SCancellableEvent, PlatformEventWrapper {

    EntityBasic getEntity();

    DamageCauseHolder getDamageCause();

    double getDamage();

    void setDamage(double damage);
}
