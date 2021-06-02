package org.screamingsandals.lib.packet;

public interface SPacketPlayOutEntityHeadRotation extends SPacket {
    void setEntityId(int entityId);

    void setRotation(byte rotation);
}
