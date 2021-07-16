package org.screamingsandals.lib.packet;

public interface SClientboundUpdateMobEffectPacket extends SPacket {

    SClientboundUpdateMobEffectPacket setEntityId(int entityId);

    SClientboundUpdateMobEffectPacket setEffectId(byte effectId);

    SClientboundUpdateMobEffectPacket setAmplifier(byte amplifier);

    SClientboundUpdateMobEffectPacket setDurationInTicks(int durationInTicks);

    SClientboundUpdateMobEffectPacket setByteFlags(boolean isAmbient, boolean shouldShowParticles);
}
