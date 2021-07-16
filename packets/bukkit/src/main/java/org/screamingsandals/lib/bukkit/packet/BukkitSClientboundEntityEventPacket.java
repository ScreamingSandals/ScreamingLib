package org.screamingsandals.lib.bukkit.packet;
import org.screamingsandals.lib.nms.accessors.ClientboundEntityEventPacketAccessor;
import org.screamingsandals.lib.packet.SClientboundEntityEventPacket;

public class BukkitSClientboundEntityEventPacket extends BukkitSPacket implements SClientboundEntityEventPacket {

    public BukkitSClientboundEntityEventPacket() {
        super(ClientboundEntityEventPacketAccessor.getType());
    }

    @Override
    public SClientboundEntityEventPacket setStatus(byte status) {
        packet.setField(ClientboundEntityEventPacketAccessor.getFieldEventId(), status);
        return this;
    }

    @Override
    public SClientboundEntityEventPacket setEntityId(int id) {
        packet.setField(ClientboundEntityEventPacketAccessor.getFieldEntityId(), id);
        return this;
    }
}
