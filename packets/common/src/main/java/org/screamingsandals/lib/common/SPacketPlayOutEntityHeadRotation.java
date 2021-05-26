package org.screamingsandals.lib.common;

public interface SPacketPlayOutEntityHeadRotation extends SPacket {
    void setEntityId(int entityId);

    void setRotation(byte rotation);
}
