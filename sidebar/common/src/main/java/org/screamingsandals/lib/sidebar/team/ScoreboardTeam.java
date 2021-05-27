package org.screamingsandals.lib.sidebar.team;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.screamingsandals.lib.packet.SPacketPlayOutScoreboardTeam;
import org.screamingsandals.lib.player.PlayerWrapper;

import java.util.List;

public interface ScoreboardTeam extends ForwardingAudience {

    ScoreboardTeam color(TextColor color);

    ScoreboardTeam displayName(Component component);

    ScoreboardTeam teamPrefix(Component teamPrefix);

    ScoreboardTeam teamSuffix(Component teamSuffix);

    ScoreboardTeam friendlyFire(boolean friendlyFire);

    ScoreboardTeam seeInvisible(boolean seeInvisible);

    ScoreboardTeam nameTagVisibility(SPacketPlayOutScoreboardTeam.TagVisibility nameTagVisibility);

    ScoreboardTeam collisionRule(SPacketPlayOutScoreboardTeam.CollisionRule collisionRule);

    ScoreboardTeam player(PlayerWrapper player);

    ScoreboardTeam removePlayer(PlayerWrapper player);

    String identifier();

    TextColor color();

    Component displayName();

    Component teamPrefix();

    Component teamSuffix();

    boolean friendlyFire();

    boolean seeInvisible();

    SPacketPlayOutScoreboardTeam.TagVisibility nameTagVisibility();

    SPacketPlayOutScoreboardTeam.CollisionRule collisionRule();

    List<PlayerWrapper> players();

    void destroy();
}
