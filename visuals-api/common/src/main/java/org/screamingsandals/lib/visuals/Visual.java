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

package org.screamingsandals.lib.visuals;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.screamingsandals.lib.player.PlayerWrapper;

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
    UUID uuid();

    /**
     * @return viewers that are currently looking and this visual.
     */
    Collection<PlayerWrapper> viewers();

    T update();

    T show();

    T hide();

    T addViewer(PlayerWrapper viewer);

    T removeViewer(PlayerWrapper viewer);

    T clearViewers();

    T title(Component title);

    T title(ComponentLike title);

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

    boolean visibleTo(PlayerWrapper player);

    void onViewerAdded(PlayerWrapper viewer, boolean checkDistance);

    void onViewerRemoved(PlayerWrapper viewer, boolean checkDistance);

    default void onViewerAdded(PlayerWrapper viewer) {
        onViewerAdded(viewer, false);
    }

    default void onViewerRemoved(PlayerWrapper viewer) {
        onViewerRemoved(viewer, false);
    }
}
