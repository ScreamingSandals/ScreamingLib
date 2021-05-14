package org.screamingsandals.lib.common;

import net.kyori.adventure.text.Component;

public interface SPacketPlayOutScoreboardObjective {

    void setObjectiveKey(Component objectiveKey);

    void setTitle(Component title);

    void setCriteria(Type criteriaType);

    void setMode(Mode mode);

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
