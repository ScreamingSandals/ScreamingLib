package org.screamingsandals.lib.bukkit;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.common.SPacketPlayOutEntityEffect;

public class BukkitSPacketPlayOutEntityEffect extends BukkitSPacket implements SPacketPlayOutEntityEffect {
    public BukkitSPacketPlayOutEntityEffect() {
        super(ClassStorage.NMS.PacketPlayOutEntityEffect);
    }

    @Override
    public void setEntityId(int entityId) {
        packet.setField("a", entityId);
    }

    @Override
    public void setEffectId(byte effectId) {
        packet.setField("b", effectId);
    }

    @Override
    public void setAmplifier(byte amplifier) {
        packet.setField("c", amplifier);
    }

    @Override
    public void setDurationInTicks(int durationInTicks) {
        packet.setField("d", durationInTicks);
    }

    @Override
    public void setByteFlags(boolean isAmbient, boolean shouldShowParticles) {
        byte flag = 0;
        if (isAmbient) {
            flag = (byte) (flag | 1);
        }

        if (shouldShowParticles) {
            flag = (byte) (flag | 2);
        }
        packet.setField("e", flag);
    }
}
