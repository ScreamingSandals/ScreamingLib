package org.screamingsandals.lib.packet;

import net.kyori.adventure.text.Component;

import java.util.UUID;

public interface SPacketPlayOutChat extends SPacket {
    SPacketPlayOutChat setChatComponent(Component text);

    SPacketPlayOutChat setBytes(byte bytes);

    SPacketPlayOutChat setUUID(UUID uuid);

    SPacketPlayOutChat setChatType(ChatType type);

    enum ChatType {
        CHAT,
        SYSTEM,
        GAME_INFO;
    }
}
