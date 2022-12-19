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

package org.screamingsandals.lib.sidebar;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.visuals.DatableVisual;

public interface ScoreSidebar extends DatableVisual<ScoreSidebar>, TeamedSidebar<ScoreSidebar> {
    /**
     * Creates new Scoreboard.
     *
     * @return created Scoreboard
     */
    @Contract(value = "-> new", pure = true)
    static @NotNull ScoreSidebar of() {
        return SidebarManager.scoreboard();
    }

    @Contract("_, _ -> this")
    @NotNull ScoreSidebar entity(@NotNull String identifier, @NotNull Component displayName);

    @Contract("_, _ -> this")
    @NotNull ScoreSidebar score(@NotNull String identifier, int score);

    @Contract("_ -> this")
    @NotNull ScoreSidebar removeEntity(@NotNull String identifier);
}
