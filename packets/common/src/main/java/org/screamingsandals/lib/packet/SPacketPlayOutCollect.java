package org.screamingsandals.lib.packet;

public interface SPacketPlayOutCollect extends SPacket {

    SPacketPlayOutCollect setEntityId(int entityId);

    SPacketPlayOutCollect setCollectedEntityId(int collectedEntityId);

    SPacketPlayOutCollect setAmount(int amount);
}
