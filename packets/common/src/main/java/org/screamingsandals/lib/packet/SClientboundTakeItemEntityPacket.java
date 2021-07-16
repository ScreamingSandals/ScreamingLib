package org.screamingsandals.lib.packet;

public interface SClientboundTakeItemEntityPacket extends SPacket {

    SClientboundTakeItemEntityPacket setItemId(int entityId);

    SClientboundTakeItemEntityPacket setCollectedEntityId(int collectedEntityId);

    SClientboundTakeItemEntityPacket setAmount(int amount);
}
