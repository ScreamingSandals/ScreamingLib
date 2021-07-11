package org.screamingsandals.lib.packet;

public interface SPacketPlayOutEntityLook extends SPacket {
    void setEntityId(int entityId);

    void setYaw(byte yaw);

    void setPitch(byte pitch);

    void setOnGround(boolean isOnGround);
}
