package org.screamingsandals.lib.packet;

public interface SPacketPlayOutEntityLook extends SPacket {
    void setEntityId(int entityId);

    void setYaw(int yaw);

    void setPitch(int pitch);

    void setOnGround(boolean isOnGround);
}
