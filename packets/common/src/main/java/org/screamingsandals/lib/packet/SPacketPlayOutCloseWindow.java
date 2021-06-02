package org.screamingsandals.lib.packet;

public interface SPacketPlayOutCloseWindow extends SPacket {
    void setWindowId(int windowId);
}
