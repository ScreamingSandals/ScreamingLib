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

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.screamingsandals.lib.sidebar.team.ScoreboardTeam;
import org.screamingsandals.lib.visuals.Visual;

import java.util.Collection;
import java.util.Optional;

public interface TeamedSidebar<T extends TeamedSidebar<T>> extends Visual<T> {

    Collection<ScoreboardTeam> getTeams();

    Optional<ScoreboardTeam> getTeam(String identifier);

    ScoreboardTeam team(String identifier);

    T removeTeam(String identifier);

    T removeTeam(ScoreboardTeam scoreboardTeam);

    T title(ComponentLike title);

    T title(Component title);
}
