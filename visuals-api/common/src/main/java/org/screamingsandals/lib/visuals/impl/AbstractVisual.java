/*
 * Copyright 2023 ScreamingSandals
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

import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.visuals.Visual;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractVisual<T extends Visual<T>> implements Visual<T> {
    protected final @NotNull List<@NotNull PlayerWrapper> viewers;
    protected final @NotNull UUID uuid;
    protected volatile boolean visible;
    @Accessors(chain = true, fluent = true)
    @Getter
    protected volatile boolean destroyed;

    public AbstractVisual(@NotNull UUID uuid) {
        this.uuid = uuid;
        this.viewers = new CopyOnWriteArrayList<>();
    }

    @Override
    public @NotNull UUID uuid() {
        return uuid;
    }

    @Override
    public @NotNull Collection<@NotNull PlayerWrapper> viewers() {
        return List.copyOf(viewers);
    }

    @Contract("_ -> this")
    @SuppressWarnings("unchecked")
    @Override
    public @NotNull T addViewer(@NotNull PlayerWrapper viewer) {
        if (viewer.isOnline() && !viewers.contains(viewer)) {
            viewers.add(viewer);
            onViewerAdded(viewer, true);
        }
        return (T) this;
    }

    @Contract("_ -> this")
    @SuppressWarnings("unchecked")
    @Override
    public @NotNull T removeViewer(@NotNull PlayerWrapper viewer) {
        if (viewers.contains(viewer)) {
            viewers.remove(viewer);
            if (viewer.isOnline()) {
                onViewerRemoved(viewer, false);
            }
        }
        return (T) this;
    }

    @Contract("-> this")
    @SuppressWarnings("unchecked")
    @Override
    public @NotNull T clearViewers() {
        hide();
        viewers.clear();
        return (T) this;
    }

    @Override
    public boolean hasViewers() {
        return !viewers.isEmpty();
    }

    @Override
    public boolean shown() {
        return visible;
    }

    @Override
    public boolean visibleTo(@NotNull PlayerWrapper player) {
        return viewers.contains(player);
    }
}
