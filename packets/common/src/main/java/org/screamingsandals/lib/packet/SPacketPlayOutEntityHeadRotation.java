package org.screamingsandals.lib.packet;

public interface SPacketPlayOutEntityHeadRotation extends SPacket {

    SPacketPlayOutEntityHeadRotation setEntityId(int entityId);

    SPacketPlayOutEntityHeadRotation setRotation(byte rotation);
}
