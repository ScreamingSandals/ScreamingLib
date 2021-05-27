package org.screamingsandals.lib.packet;

public interface SPacketPlayOutAnimation extends SPacket {
    void setEntityId(int entityId);

    void setAnimation(int animationId);
}
