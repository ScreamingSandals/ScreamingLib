package org.screamingsandals.lib.packet;

public interface SPacketPlayOutCloseWindow extends SPacket {
    SPacketPlayOutCloseWindow setWindowId(int windowId);
}
