package org.screamingsandals.lib.event.entity;

import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.SCancellableEvent;

public interface SExplosionPrimeEvent extends SCancellableEvent {

    EntityBasic getEntity();

    float getRadius();

    void setRadius(float radius);

    boolean isFire();

    void setFire(boolean fire);
}
