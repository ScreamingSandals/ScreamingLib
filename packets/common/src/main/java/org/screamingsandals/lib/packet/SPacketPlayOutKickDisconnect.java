package org.screamingsandals.lib.packet;

import net.kyori.adventure.text.Component;

public interface SPacketPlayOutKickDisconnect extends SPacket {
    void setDisconnectReason(Component reason);
}
