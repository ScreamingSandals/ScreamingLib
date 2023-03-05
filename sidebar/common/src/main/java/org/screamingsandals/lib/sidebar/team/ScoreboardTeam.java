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

package org.screamingsandals.lib.sidebar.team;

import org.screamingsandals.lib.packet.ClientboundSetPlayerTeamPacket;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.audience.PlayerAudience;

import java.util.List;

public interface ScoreboardTeam extends PlayerAudience.ForwardingToMulti {

    ScoreboardTeam color(ClientboundSetPlayerTeamPacket.TeamColor color);

    ScoreboardTeam displayName(Component component);

    ScoreboardTeam teamPrefix(Component teamPrefix);

    ScoreboardTeam teamSuffix(Component teamSuffix);

    ScoreboardTeam friendlyFire(boolean friendlyFire);

    ScoreboardTeam seeInvisible(boolean seeInvisible);

    ScoreboardTeam nameTagVisibility(ClientboundSetPlayerTeamPacket.TagVisibility nameTagVisibility);

    ScoreboardTeam collisionRule(ClientboundSetPlayerTeamPacket.CollisionRule collisionRule);

    ScoreboardTeam player(PlayerWrapper player);

    ScoreboardTeam removePlayer(PlayerWrapper player);

    String identifier();

    ClientboundSetPlayerTeamPacket.TeamColor color();

    Component displayName();

    Component teamPrefix();

    Component teamSuffix();

    boolean friendlyFire();

    boolean seeInvisible();

    ClientboundSetPlayerTeamPacket.TagVisibility nameTagVisibility();

    ClientboundSetPlayerTeamPacket.CollisionRule collisionRule();

    List<PlayerWrapper> players();

    void destroy();
}
