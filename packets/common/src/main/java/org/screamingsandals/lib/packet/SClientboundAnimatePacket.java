package org.screamingsandals.lib.packet;

public interface SClientboundAnimatePacket extends SPacket {

    SClientboundAnimatePacket setEntityId(int entityId);

    SClientboundAnimatePacket setAnimation(int animationId);
}
