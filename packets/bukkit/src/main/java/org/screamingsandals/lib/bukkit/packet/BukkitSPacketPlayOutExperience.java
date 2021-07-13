package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutExperience;

public class BukkitSPacketPlayOutExperience extends BukkitSPacket implements SPacketPlayOutExperience {

    public BukkitSPacketPlayOutExperience() {
        super(ClassStorage.NMS.PacketPlayOutExperience);
    }

    @Override
    public SPacketPlayOutExperience setExperienceBar(float exp) {
        if (exp < 0 || exp > 1) {
            throw new UnsupportedOperationException("ExperienceBar must be between 0 and 1!, provided: " + exp);
        }
        packet.setField("a,field_149401_a,f_133214_", exp);
        return this;
    }

    @Override
    public SPacketPlayOutExperience setLevel(int level) {
        packet.setField("c,field_149400_c,f_133216_", level);
        return this;
    }

    @Override
    public SPacketPlayOutExperience setTotalExperience(int totalExperience) {
        packet.setField("b,field_149399_b,f_133215_", totalExperience);
        return this;
    }
}
