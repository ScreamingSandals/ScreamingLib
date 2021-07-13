package org.screamingsandals.lib.packet;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;

import java.util.UUID;

public interface SPacketPlayOutBoss extends SPacket {
    SPacketPlayOutBoss setUniqueId(UUID uuid);

    SPacketPlayOutBoss setAction(Action action);

    SPacketPlayOutBoss setTitle(Component title);

    SPacketPlayOutBoss setHealth(float health);

    SPacketPlayOutBoss setColor(Color color);

    SPacketPlayOutBoss setDivision(Division division);

    SPacketPlayOutBoss setDarkenSky(boolean darkenSky);

    SPacketPlayOutBoss setPlayMusic(boolean playMusic);

    SPacketPlayOutBoss setCreateFog(boolean createFog);

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
