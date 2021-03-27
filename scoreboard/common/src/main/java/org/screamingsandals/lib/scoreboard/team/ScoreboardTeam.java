package org.screamingsandals.lib.scoreboard.team;

import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public interface ScoreboardTeam extends ForwardingAudience {

    ScoreboardTeam identifier(String identifier);

    ScoreboardTeam color(TextColor color);

    ScoreboardTeam displayName(Component component);

    String getIdentifier();

    TextColor getColor();

    Component getDisplayName();
}
