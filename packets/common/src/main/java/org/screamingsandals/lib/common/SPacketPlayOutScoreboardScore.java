package org.screamingsandals.lib.common;

import net.kyori.adventure.text.Component;

public interface SPacketPlayOutScoreboardScore {

    void setValue(Component value);

    void setObjectiveKey(Component objectiveKey);

    void setAction(ScoreboardAction action);

    void setScore(int score);

    enum ScoreboardAction {
        CHANGE,
        REMOVE;
    }
}
