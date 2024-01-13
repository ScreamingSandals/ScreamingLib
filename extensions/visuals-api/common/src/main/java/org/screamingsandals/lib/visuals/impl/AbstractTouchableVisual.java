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
import org.screamingsandals.lib.visuals.TouchableVisual;
import org.screamingsandals.lib.world.Location;

import java.util.UUID;

public abstract class AbstractTouchableVisual<T extends TouchableVisual<T>> extends AbstractLocatableVisual<T> implements TouchableVisual<T> {
    private volatile boolean touchable;
    private volatile long clickCoolDown;

    public AbstractTouchableVisual(@NotNull UUID uuid, @NotNull Location location, boolean touchable) {
        super(uuid, location);
        this.touchable = touchable;
        this.clickCoolDown = TouchableVisual.DEFAULT_CLICK_COOL_DOWN;
    }

    @Override
    public boolean touchable() {
        return touchable;
    }

    @Contract("_ -> this")
    @SuppressWarnings("unchecked")
    @Override
    public @NotNull T touchable(boolean touchable) {
        this.touchable = touchable;
        return (T) this;
    }

    @Override
    public long clickCooldown() {
        return clickCoolDown;
    }

    @Contract("_ -> this")
    @SuppressWarnings("unchecked")
    @Override
    public @NotNull T clickCooldown(long delay) {
        if (delay < 0) {
            return (T) this;
        }
        clickCoolDown = delay;
        return (T) this;
    }
}
