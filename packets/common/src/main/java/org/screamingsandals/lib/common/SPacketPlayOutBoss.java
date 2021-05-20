package org.screamingsandals.lib.common;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;

import java.util.UUID;

public interface SPacketPlayOutBoss {
    void setUniqueId(UUID uuid);

    void setAction(Action action);

    void setTitle(Component title);

    void setHealth(float health);

    void setColor(Color color);

    void setDivision(Division division);

    enum Action {
        ADD,
        REMOVE,
        UPDATE_HEALTH,
        UPDATE_TITLE,
        UPDATE_STYLE,
        UPDATE_FLAGS;
    }

    enum Color {
        PINK,
        BLUE,
        RED,
        GREEN,
        YELLOW,
        PURPLE,
        WHITE;
    }

    @RequiredArgsConstructor
    enum Division {
        NO_DIVISION(0),
        SIX_NOTCHES(1),
        TEN_NOTCHES(2),
        TWELVE_NOTCHES(3),
        TWENTY_NOTCHES(4);

        private final int key;
    }
}
