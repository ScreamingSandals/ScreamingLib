package org.screamingsandals.lib.packet;

import net.kyori.adventure.text.Component;

public interface SPacketPlayOutScoreboardScore extends SPacket {
    void setValue(Component value);

    void setObjectiveKey(Component objectiveKey);

    void setAction(ScoreboardAction action);

    void setScore(int score);

    enum ScoreboardAction {
        CHANGE,
        REMOVE;
    }
}
