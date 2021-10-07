package org.screamingsandals.lib.visuals.impl;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.visuals.LocatableVisual;
import org.screamingsandals.lib.world.LocationHolder;
import java.util.Objects;
import java.util.UUID;

public abstract class AbstractLocatableVisual<T extends LocatableVisual<T>> extends AbstractVisual<T> implements LocatableVisual<T> {
    private volatile boolean destroyed;
    private volatile int viewDistance;
    private volatile LocationHolder location;
    private volatile boolean created;

    public AbstractLocatableVisual(UUID uuid, LocationHolder location) {
        super(uuid);
        Objects.requireNonNull(location, "Location cannot be null!");

        // default values
        this.viewDistance = LocatableVisual.DEFAULT_VIEW_DISTANCE;
        this.destroyed = false;
        this.created = false;
        this.location = location;
    }

    @Override
    public int getViewDistance() {
        return viewDistance;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T setViewDistance(int viewDistance) {
        this.viewDistance = viewDistance;
        return (T) this;
    }

    @Override
    @NotNull
    public LocationHolder getLocation() {
        return location;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T setLocation(LocationHolder location) {
        Objects.requireNonNull(location, "Location cannot be null!");
        this.location = location;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T spawn() {
        if (created) {
            throw new UnsupportedOperationException("Visual: " + uuid.toString() + " is already spawned!");
        }
        created = true;
        return (T) this;
    }

    @Override
    public boolean isCreated() {
        return created;
    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public void destroy() {
        if (destroyed) {
            return;
        }
        destroyed = true;
    }
}
