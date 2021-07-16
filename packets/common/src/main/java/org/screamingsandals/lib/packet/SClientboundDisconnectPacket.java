package org.screamingsandals.lib.packet;

import net.kyori.adventure.text.Component;

public interface SClientboundDisconnectPacket extends SPacket {

    SClientboundDisconnectPacket setDisconnectReason(Component reason);
}
