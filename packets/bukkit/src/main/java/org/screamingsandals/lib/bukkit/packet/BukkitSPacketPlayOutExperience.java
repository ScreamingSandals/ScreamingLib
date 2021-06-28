package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutExperience;

public class BukkitSPacketPlayOutExperience extends BukkitSPacket implements SPacketPlayOutExperience {
    public BukkitSPacketPlayOutExperience() {
        super(ClassStorage.NMS.PacketPlayOutExperience);
    }

    @Override
    public void setExperienceBar(float exp) {
        if (exp < 0 || exp > 1) {
            throw new UnsupportedOperationException("ExperienceBar must be between 0 and 1!, provided: " + exp);
        }
        packet.setField("a,field_149401_a", exp);
    }

    @Override
    public void setLevel(int level) {
        packet.setField("c,field_149400_c", level);
    }

    @Override
    public void setTotalExperience(int totalExperience) {
        packet.setField("b,field_149399_b", totalExperience);
    }
}
