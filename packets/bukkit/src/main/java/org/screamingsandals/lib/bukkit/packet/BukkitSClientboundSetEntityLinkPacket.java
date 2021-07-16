package org.screamingsandals.lib.bukkit.packet;
import org.screamingsandals.lib.nms.accessors.ClientboundSetEntityLinkPacketAccessor;
import org.screamingsandals.lib.packet.SClientboundSetEntityLinkPacket;

public class BukkitSClientboundSetEntityLinkPacket extends BukkitSPacket implements SClientboundSetEntityLinkPacket {

    public BukkitSClientboundSetEntityLinkPacket() {
        super(ClientboundSetEntityLinkPacketAccessor.getType());
    }

    @Override
    public SClientboundSetEntityLinkPacket setEntityId(int entityId) {
        packet.setField(ClientboundSetEntityLinkPacketAccessor.getFieldSourceId(), entityId);
        return this;
    }

    @Override
    public SClientboundSetEntityLinkPacket setHoldingEntityId(int entityId) {
        packet.setField(ClientboundSetEntityLinkPacketAccessor.getFieldDestId(), entityId);
        return this;
    }
}
