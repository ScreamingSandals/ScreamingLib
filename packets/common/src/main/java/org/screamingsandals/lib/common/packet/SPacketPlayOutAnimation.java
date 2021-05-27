package org.screamingsandals.lib.common.packet;

public interface SPacketPlayOutAnimation extends SPacket {
    void setEntityId(int entityId);

    void setAnimation(int animationId);
}
