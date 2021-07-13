package org.screamingsandals.lib.packet;

public interface SPacketPlayOutExperience extends SPacket {

    SPacketPlayOutExperience setExperienceBar(float exp);

    SPacketPlayOutExperience setLevel(int level);

    SPacketPlayOutExperience setTotalExperience(int totalExperience);
}
