package org.screamingsandals.lib.packet;

import net.kyori.adventure.text.Component;

public interface SPacketPlayOutScoreboardObjective extends SPacket {

    SPacketPlayOutScoreboardObjective setObjectiveKey(Component objectiveKey);

    SPacketPlayOutScoreboardObjective setTitle(Component title);

    SPacketPlayOutScoreboardObjective setCriteria(Type criteriaType);

    SPacketPlayOutScoreboardObjective setMode(Mode mode);

    enum Type {
        INTEGER,
        HEARTS;
    }

    enum Mode {
        CREATE,
        DESTROY,
        UPDATE
    }
}
