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

package org.screamingsandals.lib.visuals;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;

import java.util.Collection;
import java.util.UUID;

/**
 * Represents Visual that can have title and viewers.
 */
public interface Visual<T> {

    /**
     * Each visual can have its UUID.
     *
     * @return UUID of this visual.
     */
    @NotNull UUID uuid();

    /**
     * @return viewers that are currently looking and this visual.
     */
    @NotNull Collection<@NotNull Player> viewers();

    @Contract("-> this")
    default @NotNull T update() {
        return update(UpdateStrategy.ALL);
    }

    @Contract("_ -> this")
    @NotNull T update(@NotNull UpdateStrategy strategy);

    @Contract("-> this")
    @NotNull T show();

    @Contract("-> this")
    @NotNull T hide();
    @Contract("_ -> this")
    @NotNull T addViewer(@NotNull Player viewer);

    @Contract("_ -> this")
    @NotNull T removeViewer(@NotNull Player viewer);

    @Contract("-> this")
    @NotNull T clearViewers();

    @Contract("_ -> this")
    @NotNull T title(@NotNull Component title);

    @Contract("_ -> this")
    @NotNull T title(@NotNull ComponentLike title);

    /**
     * Checks if this Visual has any viewers.
     *
     * @return true if yes, duh.
     */
    boolean hasViewers();

    boolean shown();

    void destroy();

    /**
     *
     * @return true if this Visual has been destroyed, false otherwise
     */
    boolean destroyed();

    boolean visibleTo(@NotNull Player player);

    void onViewerAdded(@NotNull Player viewer, boolean checkDistance);

    void onViewerRemoved(@NotNull Player viewer, boolean checkDistance);

    default void onViewerAdded(@NotNull Player viewer) {
        onViewerAdded(viewer, false);
    }

    default void onViewerRemoved(@NotNull Player viewer) {
        onViewerRemoved(viewer, false);
    }
}
