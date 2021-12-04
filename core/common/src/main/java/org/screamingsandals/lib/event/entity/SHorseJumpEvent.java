package org.screamingsandals.lib.event.entity;

import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.SCancellableEvent;

public interface SHorseJumpEvent extends SCancellableEvent {

    EntityBasic getEntity();

    float getPower();

    @Deprecated
    void setPower(float power);
}
