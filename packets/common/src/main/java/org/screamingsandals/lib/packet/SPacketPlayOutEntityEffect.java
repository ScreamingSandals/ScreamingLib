package org.screamingsandals.lib.packet;

public interface SPacketPlayOutEntityEffect extends SPacket {

    SPacketPlayOutEntityEffect setEntityId(int entityId);

    SPacketPlayOutEntityEffect setEffectId(byte effectId);

    SPacketPlayOutEntityEffect setAmplifier(byte amplifier);

    SPacketPlayOutEntityEffect setDurationInTicks(int durationInTicks);

    SPacketPlayOutEntityEffect setByteFlags(boolean isAmbient,  boolean shouldShowParticles);
}
