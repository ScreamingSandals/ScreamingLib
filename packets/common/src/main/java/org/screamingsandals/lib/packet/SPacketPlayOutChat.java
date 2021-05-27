package org.screamingsandals.lib.packet;

import net.kyori.adventure.text.Component;

public interface SPacketPlayOutChat extends SPacket {
    void setChatComponent(Component text);

    void setBytes(byte bytes);
}
