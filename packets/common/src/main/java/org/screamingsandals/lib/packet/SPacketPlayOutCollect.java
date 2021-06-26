package org.screamingsandals.lib.packet;

public interface SPacketPlayOutCollect extends SPacket {
    void setEntityId(int entityId);

    void setCollectedEntityId(int collectedEntityId);

    void setAmount(int amount);
}
