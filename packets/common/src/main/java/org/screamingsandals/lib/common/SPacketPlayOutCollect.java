package org.screamingsandals.lib.common;

public interface SPacketPlayOutCollect extends SPacket {
    void setEntityId(int entityId);

    void setCollectedEntityId(int collectedEntityId);
}
