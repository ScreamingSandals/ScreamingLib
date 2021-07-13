package org.screamingsandals.lib.packet;

import net.kyori.adventure.text.Component;

public interface SPacketPlayOutScoreboardScore extends SPacket {

    SPacketPlayOutScoreboardScore setValue(Component value);

    SPacketPlayOutScoreboardScore setObjectiveKey(Component objectiveKey);

    SPacketPlayOutScoreboardScore setAction(ScoreboardAction action);

    SPacketPlayOutScoreboardScore setScore(int score);

    enum ScoreboardAction {
        CHANGE,
        REMOVE;
    }
}
