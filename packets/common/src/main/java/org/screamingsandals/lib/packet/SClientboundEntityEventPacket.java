package org.screamingsandals.lib.packet;

public interface SClientboundEntityEventPacket extends SPacket {
    SClientboundEntityEventPacket setStatus(byte status);

    SClientboundEntityEventPacket setEntityId(int id);
}
