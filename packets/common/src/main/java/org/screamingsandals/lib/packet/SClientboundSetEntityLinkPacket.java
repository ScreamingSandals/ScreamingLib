package org.screamingsandals.lib.packet;

public interface SClientboundSetEntityLinkPacket extends SPacket {

    SClientboundSetEntityLinkPacket setEntityId(int entityId);

    SClientboundSetEntityLinkPacket setHoldingEntityId(int entityId);
}
