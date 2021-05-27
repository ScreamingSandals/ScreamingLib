package org.screamingsandals.lib.common.packet;

public interface SPacketPlayOutExperience extends SPacket {
    void setExperienceBar(float exp);

    void setLevel(int level);

    void setTotalExperience(int totalExperience);
}
