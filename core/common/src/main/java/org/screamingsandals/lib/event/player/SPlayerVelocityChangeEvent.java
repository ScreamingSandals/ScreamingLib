package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.utils.math.Vector3D;

public interface SPlayerVelocityChangeEvent extends SCancellableEvent, SPlayerEvent, PlatformEventWrapper {

    Vector3D getVelocity();

    void setVelocity(Vector3D velocity);
}
