package org.screamingsandals.lib.scoreboard.team;

import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.screamingsandals.lib.scoreboard.Scoreboard;

public interface ScoreboardTeam extends ForwardingAudience {

    String getIdentifier();

    void setIdentifier(String identifier);

    TextColor getColor();

    void setColor(TextColor color);

    Component getDisplayName();

    void setDisplayName(Component component);

    interface Builder {

        Builder identifier(String identifier);

        Builder color(TextColor color);

        Builder displayName(Component displayName);

        Scoreboard build();
    }
}
