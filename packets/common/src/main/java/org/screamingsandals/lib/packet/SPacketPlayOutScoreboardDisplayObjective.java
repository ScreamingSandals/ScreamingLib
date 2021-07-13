package org.screamingsandals.lib.packet;

import net.kyori.adventure.text.Component;

public interface SPacketPlayOutScoreboardDisplayObjective extends SPacket {
    SPacketPlayOutScoreboardDisplayObjective setObjectiveKey(Component objectiveKey);

    SPacketPlayOutScoreboardDisplayObjective setDisplaySlot(DisplaySlot slot);

    enum DisplaySlot {
        PLAYER_LIST,
        SIDEBAR,
        BELOW_NAME
    }
}
