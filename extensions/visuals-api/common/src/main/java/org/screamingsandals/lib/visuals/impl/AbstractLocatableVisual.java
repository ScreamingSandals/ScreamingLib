/*
 * Copyright 2024 ScreamingSandals
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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.visuals.LocatableVisual;
import org.screamingsandals.lib.visuals.UpdateStrategy;
import org.screamingsandals.lib.world.Location;

import java.util.Objects;
import java.util.UUID;

@SuppressWarnings("unchecked") // this is just java being dum
public abstract class AbstractLocatableVisual<T extends LocatableVisual<T>> extends AbstractVisual<T> implements LocatableVisual<T> {
    private volatile int viewDistance;
    private volatile @NotNull Location location;
    private volatile boolean created;

    public AbstractLocatableVisual(@NotNull UUID uuid, @NotNull Location location) {
        super(uuid);
        Objects.requireNonNull(location, "Location cannot be null!");
        this.viewDistance = LocatableVisual.DEFAULT_VIEW_DISTANCE;
        this.location = location;
    }

    @Override
    public boolean created() {
        return created;
    }

    @Override
    public int viewDistance() {
        return viewDistance;
    }

    @Contract("_ -> this")
    @Override
    public @NotNull T viewDistance(int viewDistance) {
        this.viewDistance = viewDistance;
        return (T) this;
    }

    @Override
    public @NotNull Location location() {
        return location;
    }

    @Override
    public @NotNull T location(@NotNull Location location) {
        Objects.requireNonNull(location, "Location cannot be null!");
        this.location = location;
        update(UpdateStrategy.POSITION);
        return (T) this;
    }

    @Contract("-> this")
    @Override
    public @NotNull T spawn() {
        if (created) {
            throw new UnsupportedOperationException("Visual: " + uuid + " is already spawned!");
        }
        show();
        created = true;
        return (T) this;
    }

    @Override
    public void destroy() {
        if (destroyed) {
            throw new UnsupportedOperationException("Visual: " + uuid + " is already destroyed!");
        }
        hide();
        destroyed = true;
    }
}
