package org.screamingsandals.lib.packet;

public interface SClientboundKeepAlivePacket extends SPacket {

    SClientboundKeepAlivePacket setEntityId(int entityId);
}
