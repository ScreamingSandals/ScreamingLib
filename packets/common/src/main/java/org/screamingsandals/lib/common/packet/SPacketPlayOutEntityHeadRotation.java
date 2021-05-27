package org.screamingsandals.lib.common.packet;

public interface SPacketPlayOutEntityHeadRotation extends SPacket {
    void setEntityId(int entityId);

    void setRotation(byte rotation);
}
