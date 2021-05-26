package org.screamingsandals.lib.common;

public interface SPacketPlayOutAnimation extends SPacket {
    void setEntityId(int entityId);

    void setAnimation(int animationId);
}
