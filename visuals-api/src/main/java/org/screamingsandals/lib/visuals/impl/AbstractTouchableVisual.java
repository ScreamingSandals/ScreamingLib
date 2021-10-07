package org.screamingsandals.lib.visuals.impl;

import org.screamingsandals.lib.visuals.TouchableVisual;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.UUID;

public abstract class AbstractTouchableVisual<T extends TouchableVisual<T>> extends AbstractLocatableVisual<T> implements TouchableVisual<T> {
    private volatile boolean touchable;
    private volatile long clickCoolDown;

    public AbstractTouchableVisual(UUID uuid, LocationHolder location) {
        super(uuid, location);
        this.clickCoolDown = TouchableVisual.DEFAULT_CLICK_COOL_DOWN;
    }

    @Override
    public boolean isTouchable() {
        return touchable;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T setTouchable(boolean touchable) {
        this.touchable = touchable;
        return (T) this;
    }

    @Override
    public long getClickCoolDown() {
        return clickCoolDown;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T setClickCoolDown(long delay) {
        if (delay < 0) {
            return (T) this;
        }
        clickCoolDown = delay;
        return (T) this;
    }
}
