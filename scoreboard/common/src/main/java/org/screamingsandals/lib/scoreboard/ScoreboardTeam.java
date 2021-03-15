package org.screamingsandals.lib.scoreboard;

import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public interface ScoreboardTeam extends ForwardingAudience {

    TextColor getColor();

    void setColor(TextColor color);

    Component getDisplayName();

    void setDisplayName(Component component);
}
