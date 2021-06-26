package org.screamingsandals.lib.packet;

import net.kyori.adventure.text.Component;

import java.util.UUID;

public interface SPacketPlayOutChat extends SPacket {
    void setChatComponent(Component text);

    void setBytes(byte bytes);

    void setUUID(UUID uuid);

    void setChatType(ChatType type);

    enum ChatType {
        CHAT,
        SYSTEM,
        GAME_INFO;
    }
}
