package org.screamingsandals.lib.sidebar.team;

import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.screamingsandals.lib.packet.SClientboundSetPlayerTeamPacket;
import org.screamingsandals.lib.player.PlayerWrapper;

import java.util.List;

public interface ScoreboardTeam extends ForwardingAudience {

    ScoreboardTeam color(NamedTextColor color);

    ScoreboardTeam displayName(Component component);

    ScoreboardTeam teamPrefix(Component teamPrefix);

    ScoreboardTeam teamSuffix(Component teamSuffix);

    ScoreboardTeam friendlyFire(boolean friendlyFire);

    ScoreboardTeam seeInvisible(boolean seeInvisible);

    ScoreboardTeam nameTagVisibility(SClientboundSetPlayerTeamPacket.TagVisibility nameTagVisibility);

    ScoreboardTeam collisionRule(SClientboundSetPlayerTeamPacket.CollisionRule collisionRule);

    ScoreboardTeam player(PlayerWrapper player);

    ScoreboardTeam removePlayer(PlayerWrapper player);

    String identifier();

    NamedTextColor color();

    Component displayName();

    Component teamPrefix();

    Component teamSuffix();

    boolean friendlyFire();

    boolean seeInvisible();

    SClientboundSetPlayerTeamPacket.TagVisibility nameTagVisibility();

    SClientboundSetPlayerTeamPacket.CollisionRule collisionRule();

    List<PlayerWrapper> players();

    void destroy();
}
