package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.nms.accessors.ClientboundKeepAlivePacketAccessor;
import org.screamingsandals.lib.packet.SClientboundKeepAlivePacket;

public class BukkitSClientboundKeepAlivePacket extends BukkitSPacket implements SClientboundKeepAlivePacket {

    public BukkitSClientboundKeepAlivePacket() {
        super(ClientboundKeepAlivePacketAccessor.getType());
    }

    @Override
    public SClientboundKeepAlivePacket setEntityId(int entityId) {
        packet.setField(ClientboundKeepAlivePacketAccessor.getFieldId(), entityId);
        return this;
    }
}
