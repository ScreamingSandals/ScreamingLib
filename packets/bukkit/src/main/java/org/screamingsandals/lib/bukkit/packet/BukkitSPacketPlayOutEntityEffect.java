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
        if (Version.isVersion(1, 17)) {
            packet.setField("d", entityId);
        } else {
            packet.setField("a,field_149434_a", entityId);
        }
    }

    @Override
    public void setEffectId(byte effectId) {
        if (Version.isVersion(1, 17)) {
            packet.setField("e", effectId);
        } else {
            packet.setField("b,field_149432_b", effectId);
        }
    }

    @Override
    public void setAmplifier(byte amplifier) {
        if (Version.isVersion(1, 17)) {
            packet.setField("f", amplifier);
        } else {
            packet.setField("c,field_149433_c", amplifier);
        }
    }

    @Override
    public void setDurationInTicks(int durationInTicks) {
        if (Version.isVersion(1, 17)) {
            packet.setField("g", durationInTicks);
        } else {
            packet.setField("d,field_149431_d", durationInTicks);
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
            packet.setField("e,field_186985_e", flag);
        }
    }
}
