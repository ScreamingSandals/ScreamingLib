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

import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.visuals.Visual;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractVisual<T extends Visual<T>> implements Visual<T> {
    protected final List<PlayerWrapper> viewers;
    protected final UUID uuid;
    protected volatile boolean visible;

    public AbstractVisual(UUID uuid) {
        this.uuid = uuid;
        this.visible = false;
        this.viewers = new CopyOnWriteArrayList<>();
    }

    protected abstract void update0();

    @Override
    public UUID uuid() {
        return uuid;
    }

    @Override
    public Collection<PlayerWrapper> viewers() {
        return List.copyOf(viewers);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T addViewer(PlayerWrapper viewer) {
        if (!viewers.contains(viewer)) {
            viewers.add(viewer);
            onViewerAdded(viewer, true);
        }
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T removeViewer(PlayerWrapper viewer) {
        if (viewers.contains(viewer)) {
            viewers.remove(viewer);
            onViewerRemoved(viewer, false);
        }
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T clearViewers() {
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
    public boolean visibleTo(PlayerWrapper player) {
        return viewers.contains(player);
    }
}
