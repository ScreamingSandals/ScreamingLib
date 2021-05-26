package org.screamingsandals.lib.common;

public interface SPacketPlayOutGameStateChange extends SPacket {
    void setReason(int reason);

    void setValue(float value);
}
