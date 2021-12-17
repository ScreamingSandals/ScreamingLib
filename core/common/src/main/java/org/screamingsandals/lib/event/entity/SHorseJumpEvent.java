package org.screamingsandals.lib.event.entity;

import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;

public interface SHorseJumpEvent extends SCancellableEvent, PlatformEventWrapper {

    EntityBasic getEntity();

    float getPower();

    @Deprecated
    void setPower(float power);
}
