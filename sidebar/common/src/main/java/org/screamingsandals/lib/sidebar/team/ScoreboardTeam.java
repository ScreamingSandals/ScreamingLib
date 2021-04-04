package org.screamingsandals.lib.sidebar.team;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.screamingsandals.lib.player.PlayerWrapper;

import java.util.List;

public interface ScoreboardTeam extends ForwardingAudience {

    ScoreboardTeam color(TextColor color);

    ScoreboardTeam displayName(Component component);

    ScoreboardTeam teamPrefix(Component teamPrefix);

    ScoreboardTeam teamSuffix(Component teamSuffix);

    ScoreboardTeam friendlyFire(boolean friendlyFire);

    ScoreboardTeam seeInvisible(boolean seeInvisible);

    ScoreboardTeam nameTagVisibility(NameTagVisibility nameTagVisibility);

    ScoreboardTeam collisionRule(CollisionRule collisionRule);

    ScoreboardTeam player(PlayerWrapper player);

    ScoreboardTeam removePlayer(PlayerWrapper player);

    String identifier();

    TextColor color();

    Component displayName();

    Component teamPrefix();

    Component teamSuffix();

    boolean friendlyFire();

    boolean seeInvisible();

    NameTagVisibility nameTagVisibility();

    CollisionRule collisionRule();

    List<PlayerWrapper> players();

    void destroy();

    @RequiredArgsConstructor
    @Getter
    enum NameTagVisibility {
        ALWAYS("always"),
        HIDE_FOR_OTHER_TEAMS("hideForOtherTeams"),
        HIDE_FOR_OWN_TEAM("hideForOwnTeam"),
        NEVER("never");

        private final String identifier;
    }

    @RequiredArgsConstructor
    @Getter
    enum CollisionRule {
        ALWAYS("always"),
        PUSH_OTHER_TEAMS("pushOtherTeams"),
        PUSH_OWN_TEAM("pushOwnTeam"),
        NEVER("never");

        private final String identifier;
    }
}
