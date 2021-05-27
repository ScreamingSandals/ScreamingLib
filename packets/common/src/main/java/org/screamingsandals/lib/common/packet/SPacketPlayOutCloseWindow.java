package org.screamingsandals.lib.common.packet;

public interface SPacketPlayOutCloseWindow extends SPacket {
    void setWindowId(int windowId);
}
