package org.screamingsandals.lib.packet;

public interface SPacketPlayOutEntityEffect extends SPacket {
    void setEntityId(int entityId);

    void setEffectId(byte effectId);

    void setAmplifier(byte amplifier);

    void setDurationInTicks(int durationInTicks);

    void setByteFlags(boolean isAmbient,  boolean shouldShowParticles);
}
