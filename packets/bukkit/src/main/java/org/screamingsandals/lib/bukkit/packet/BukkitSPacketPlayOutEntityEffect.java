package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.packet.SPacketPlayOutEntityEffect;

public class BukkitSPacketPlayOutEntityEffect extends BukkitSPacket implements SPacketPlayOutEntityEffect {

    public BukkitSPacketPlayOutEntityEffect() {
        super(ClassStorage.NMS.PacketPlayOutEntityEffect);
    }

    @Override
    public SPacketPlayOutEntityEffect setEntityId(int entityId) {
        if (Version.isVersion(1, 17)) {
            packet.setField("d,f_133604_", entityId);
        } else {
            packet.setField("a,field_149434_a", entityId);
        }
        return this;
    }

    @Override
    public SPacketPlayOutEntityEffect setEffectId(byte effectId) {
        if (Version.isVersion(1, 17)) {
            packet.setField("e,f_133605_", effectId);
        } else {
            packet.setField("b,field_149432_b", effectId);
        }
        return this;
    }

    @Override
    public SPacketPlayOutEntityEffect setAmplifier(byte amplifier) {
        if (Version.isVersion(1, 17)) {
            packet.setField("f,f_133606_", amplifier);
        } else {
            packet.setField("c,field_149433_c", amplifier);
        }
        return this;
    }

    @Override
    public SPacketPlayOutEntityEffect setDurationInTicks(int durationInTicks) {
        if (Version.isVersion(1, 17)) {
            packet.setField("g,f_133607_", durationInTicks);
        } else {
            packet.setField("d,field_149431_d", durationInTicks);
        }
        return this;
    }

    @Override
    public SPacketPlayOutEntityEffect setByteFlags(boolean isAmbient, boolean shouldShowParticles) {
        byte flag = 0;
        if (isAmbient) {
            flag = (byte) (flag | 1);
        }

        if (shouldShowParticles) {
            flag = (byte) (flag | 2);
        }
        if (Version.isVersion(1, 17)) {
            packet.setField("h,f_133608_", flag);
        } else {
            packet.setField("e,field_186985_e", flag);
        }
        return this;
    }
}
