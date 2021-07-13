package org.screamingsandals.lib.packet;

public interface SPacketPlayOutEntityStatus extends SPacket {
    SPacketPlayOutEntityStatus setStatus(byte status);

    SPacketPlayOutEntityStatus setEntityId(int id);
}
