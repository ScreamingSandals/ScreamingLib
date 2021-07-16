package org.screamingsandals.lib.bukkit.packet;
import org.screamingsandals.lib.nms.accessors.ClientboundUpdateMobEffectPacketAccessor;
import org.screamingsandals.lib.packet.SClientboundUpdateMobEffectPacket;

public class BukkitSClientboundUpdateMobEffectPacket extends BukkitSPacket implements SClientboundUpdateMobEffectPacket {

    public BukkitSClientboundUpdateMobEffectPacket() {
        super(ClientboundUpdateMobEffectPacketAccessor.getType());
    }

    @Override
    public SClientboundUpdateMobEffectPacket setEntityId(int entityId) {
        packet.setField(ClientboundUpdateMobEffectPacketAccessor.getFieldEntityId(), entityId);
        return this;
    }

    @Override
    public SClientboundUpdateMobEffectPacket setEffectId(byte effectId) {
        packet.setField(ClientboundUpdateMobEffectPacketAccessor.getFieldEffectId(), effectId);
        return this;
    }

    @Override
    public SClientboundUpdateMobEffectPacket setAmplifier(byte amplifier) {
        packet.setField(ClientboundUpdateMobEffectPacketAccessor.getFieldEffectAmplifier(), amplifier);
        return this;
    }

    @Override
    public SClientboundUpdateMobEffectPacket setDurationInTicks(int durationInTicks) {
        packet.setField(ClientboundUpdateMobEffectPacketAccessor.getFieldEffectDurationTicks(), durationInTicks);
        return this;
    }

    @Override
    public SClientboundUpdateMobEffectPacket setByteFlags(boolean isAmbient, boolean shouldShowParticles) {
        byte flag = 0;
        if (isAmbient) {
            flag = (byte) (flag | 1);
        }

        if (shouldShowParticles) {
            flag = (byte) (flag | 2);
        }
        packet.setField(ClientboundUpdateMobEffectPacketAccessor.getFieldFlags(), flag);
        return this;
    }
}
