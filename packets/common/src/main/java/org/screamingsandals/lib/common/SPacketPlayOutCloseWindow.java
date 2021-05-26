package org.screamingsandals.lib.common;

public interface SPacketPlayOutCloseWindow extends SPacket {
    void setWindowId(int windowId);
}
