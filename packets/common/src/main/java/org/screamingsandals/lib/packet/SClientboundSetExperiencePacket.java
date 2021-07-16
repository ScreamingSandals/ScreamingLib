package org.screamingsandals.lib.packet;

public interface SClientboundSetExperiencePacket extends SPacket {

    SClientboundSetExperiencePacket setExperienceProgress(float exp);

    SClientboundSetExperiencePacket setLevel(int level);

    SClientboundSetExperiencePacket setTotalExperience(int totalExperience);
}
