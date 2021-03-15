package org.screamingsandals.lib.scoreboard.line;

import net.kyori.adventure.text.Component;

public interface ScoreboardLine {

    static ScoreboardLine of(String identifier, Component text) {
        return SimpleScoreboardLine.of(identifier, text);
    }

    String getIdentifier();

    Component getText();

    void setText(Component text);
}
