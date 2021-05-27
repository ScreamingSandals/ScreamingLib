package org.screamingsandals.lib.common.packet;

public interface SPacketPlayOutGameStateChange extends SPacket {
    void setReason(int reason);

    void setValue(float value);
}
