package org.screamingsandals.lib.packet;

import net.kyori.adventure.text.Component;

public interface SClientboundSetScorePacket extends SPacket {

    SClientboundSetScorePacket setValue(Component value);

    SClientboundSetScorePacket setObjectiveKey(Component objectiveKey);

    SClientboundSetScorePacket setAction(ScoreboardAction action);

    SClientboundSetScorePacket setScore(int score);

    enum ScoreboardAction {
        CHANGE,
        REMOVE;
    }
}
