package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.packet.SPacketPlayOutEntityEffect;

public class BukkitSPacketPlayOutEntityEffect extends BukkitSPacket implements SPacketPlayOutEntityEffect {
    public BukkitSPacketPlayOutEntityEffect() {
        super(ClassStorage.NMS.PacketPlayOutEntityEffect);
    }

    @Override
    public void setEntityId(int entityId) {
        if (packet.setField("a", entityId) == null) {
            packet.setField("d", entityId);
        }
    }

    @Override
    public void setEffectId(byte effectId) {
        if (packet.setField("b", effectId) == null) {
            packet.setField("e", effectId);
        }
    }

    @Override
    public void setAmplifier(byte amplifier) {
        if (packet.setField("c", amplifier) == null) {
            packet.setField("f", amplifier);
        }
    }

    @Override
    public void setDurationInTicks(int durationInTicks) {
        if (Version.isVersion(1, 17)) {
            packet.setField("g", durationInTicks);
        } else {
            packet.setField("d", durationInTicks);
        }
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
        if (Version.isVersion(1, 17)) {
            packet.setField("h", flag);
        } else {
            packet.setField("e", flag);
        }
    }
}
