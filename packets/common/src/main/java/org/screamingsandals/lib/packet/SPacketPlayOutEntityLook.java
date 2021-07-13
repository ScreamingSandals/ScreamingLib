package org.screamingsandals.lib.packet;

public interface SPacketPlayOutEntityLook extends SPacket {

    SPacketPlayOutEntityLook setEntityId(int entityId);

    SPacketPlayOutEntityLook setYaw(byte yaw);

    SPacketPlayOutEntityLook setPitch(byte pitch);

    SPacketPlayOutEntityLook setOnGround(boolean isOnGround);
}
