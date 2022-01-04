/*
 * Copyright 2022 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
