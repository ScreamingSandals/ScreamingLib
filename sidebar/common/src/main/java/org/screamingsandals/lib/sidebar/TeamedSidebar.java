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

package org.screamingsandals.lib.sidebar;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.sidebar.team.ScoreboardTeam;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;
import org.screamingsandals.lib.visuals.Visual;

import java.util.Collection;

public interface TeamedSidebar<T extends TeamedSidebar<T>> extends Visual<T> {

    @NotNull Collection<@NotNull ScoreboardTeam> getTeams();

    @Nullable ScoreboardTeam getTeam(@NotNull String identifier);

    @NotNull ScoreboardTeam team(@NotNull String identifier);

    @Contract("_ -> this")
    @NotNull T removeTeam(@NotNull String identifier);

    @Contract("_ -> this")
    @NotNull T removeTeam(@NotNull ScoreboardTeam scoreboardTeam);

    @Contract("_ -> this")
    @NotNull T title(@NotNull ComponentLike title);

    @Contract("_ -> this")
    @NotNull
    T title(@NotNull Component title);
}
