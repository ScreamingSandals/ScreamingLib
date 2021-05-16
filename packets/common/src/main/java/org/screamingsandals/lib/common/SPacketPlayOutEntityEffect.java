package org.screamingsandals.lib.common;

public interface SPacketPlayOutEntityEffect {
    void setEntityId(int entityId);

    void setEffectId(byte effectId);

    void setAmplifier(byte amplifier);

    void setDurationInTicks(int durationInTicks);

    void setByteFlags(boolean isAmbient,  boolean shouldShowParticles);
}
