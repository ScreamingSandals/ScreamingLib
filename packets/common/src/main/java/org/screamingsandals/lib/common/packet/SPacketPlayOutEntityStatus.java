package org.screamingsandals.lib.common.packet;

public interface SPacketPlayOutEntityStatus extends SPacket {
    void setStatus(byte status);

    void setEntityId(int id);
}
