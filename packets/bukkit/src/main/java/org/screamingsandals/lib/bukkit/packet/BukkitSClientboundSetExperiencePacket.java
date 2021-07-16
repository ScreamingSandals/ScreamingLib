package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.nms.accessors.ClientboundSetExperiencePacketAccessor;
import org.screamingsandals.lib.packet.SClientboundSetExperiencePacket;

public class BukkitSClientboundSetExperiencePacket extends BukkitSPacket implements SClientboundSetExperiencePacket {

    public BukkitSClientboundSetExperiencePacket() {
        super(ClientboundSetExperiencePacketAccessor.getType());
    }

    @Override
    public SClientboundSetExperiencePacket setExperienceProgress(float exp) {
        if (exp < 0 || exp > 1) {
            throw new UnsupportedOperationException("ExperienceBar must be between 0 and 1!, provided: " + exp);
        }

        packet.setField(ClientboundSetExperiencePacketAccessor.getFieldExperienceProgress(), exp);
        return this;
    }

    @Override
    public SClientboundSetExperiencePacket setLevel(int level) {
        packet.setField(ClientboundSetExperiencePacketAccessor.getFieldExperienceLevel(), level);
        return this;
    }

    @Override
    public SClientboundSetExperiencePacket setTotalExperience(int totalExperience) {
        packet.setField(ClientboundSetExperiencePacketAccessor.getFieldTotalExperience(), totalExperience);
        return this;
    }
}
