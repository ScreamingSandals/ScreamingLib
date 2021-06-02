package org.screamingsandals.lib.packet;

public interface SPacketPlayOutEntityStatus extends SPacket {
    void setStatus(byte status);

    void setEntityId(int id);
}
