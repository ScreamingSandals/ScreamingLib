package org.screamingsandals.lib.packet;

public interface SClientboundRotateHeadPacket extends SPacket {

    SClientboundRotateHeadPacket setEntityId(int entityId);

    SClientboundRotateHeadPacket setRotation(byte rotation);
}
