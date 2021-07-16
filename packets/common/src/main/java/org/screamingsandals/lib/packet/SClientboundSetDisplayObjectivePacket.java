package org.screamingsandals.lib.packet;

import net.kyori.adventure.text.Component;

public interface SClientboundSetDisplayObjectivePacket extends SPacket {
    SClientboundSetDisplayObjectivePacket setObjectiveKey(Component objectiveKey);

    SClientboundSetDisplayObjectivePacket setDisplaySlot(DisplaySlot slot);

    enum DisplaySlot {
        PLAYER_LIST,
        SIDEBAR,
        BELOW_NAME
    }
}
