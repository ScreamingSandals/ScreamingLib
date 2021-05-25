package org.screamingsandals.lib.common;

import net.kyori.adventure.text.Component;

public interface SPacketPlayOutScoreboardDisplayObjective {
    void setObjectiveKey(Component objectiveKey);

    void setDisplaySlot(DisplaySlot slot);

    enum DisplaySlot {
        PLAYER_LIST,
        SIDEBAR,
        BELOW_NAME
    }
}
