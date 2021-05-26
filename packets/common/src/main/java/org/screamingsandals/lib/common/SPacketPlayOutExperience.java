package org.screamingsandals.lib.common;

public interface SPacketPlayOutExperience extends SPacket {
    void setExperienceBar(float exp);

    void setLevel(int level);

    void setTotalExperience(int totalExperience);
}
