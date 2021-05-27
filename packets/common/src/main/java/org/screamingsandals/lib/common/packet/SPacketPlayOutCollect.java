package org.screamingsandals.lib.common.packet;

public interface SPacketPlayOutCollect extends SPacket {
    void setEntityId(int entityId);

    void setCollectedEntityId(int collectedEntityId);
}
