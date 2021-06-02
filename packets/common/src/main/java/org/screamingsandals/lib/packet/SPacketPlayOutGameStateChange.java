package org.screamingsandals.lib.packet;

public interface SPacketPlayOutGameStateChange extends SPacket {
    void setReason(int reason);

    void setValue(float value);
}
