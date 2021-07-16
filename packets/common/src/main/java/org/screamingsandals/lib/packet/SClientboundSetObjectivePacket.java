package org.screamingsandals.lib.packet;

import net.kyori.adventure.text.Component;

public interface SClientboundSetObjectivePacket extends SPacket {

    SClientboundSetObjectivePacket setObjectiveKey(Component objectiveKey);

    SClientboundSetObjectivePacket setTitle(Component title);

    SClientboundSetObjectivePacket setCriteria(Type criteriaType);

    SClientboundSetObjectivePacket setMode(Mode mode);

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
