package org.screamingsandals.lib.packet;

public interface SClientboundContainerClosePacket extends SPacket {
    SClientboundContainerClosePacket setWindowId(int windowId);
}
