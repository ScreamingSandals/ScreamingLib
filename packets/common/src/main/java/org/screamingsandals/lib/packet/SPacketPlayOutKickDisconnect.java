package org.screamingsandals.lib.packet;

import net.kyori.adventure.text.Component;

public interface SPacketPlayOutKickDisconnect extends SPacket {

    SPacketPlayOutKickDisconnect setDisconnectReason(Component reason);
}
