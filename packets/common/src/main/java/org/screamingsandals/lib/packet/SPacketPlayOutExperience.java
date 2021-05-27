package org.screamingsandals.lib.packet;

public interface SPacketPlayOutExperience extends SPacket {
    void setExperienceBar(float exp);

    void setLevel(int level);

    void setTotalExperience(int totalExperience);
}
