package org.screamingsandals.lib.packet;

public interface SPacketPlayOutAnimation extends SPacket {

    SPacketPlayOutAnimation setEntityId(int entityId);

    SPacketPlayOutAnimation setAnimation(int animationId);
}
