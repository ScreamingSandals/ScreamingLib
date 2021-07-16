package org.screamingsandals.lib.bukkit.packet;
import org.screamingsandals.lib.nms.accessors.ClientboundRotateHeadPacketAccessor;
import org.screamingsandals.lib.packet.SClientboundRotateHeadPacket;

public class BukkitSClientboundRotateHeadPacket extends BukkitSPacket implements SClientboundRotateHeadPacket {

    public BukkitSClientboundRotateHeadPacket() {
        super(ClientboundRotateHeadPacketAccessor.getType());
    }

    @Override
    public SClientboundRotateHeadPacket setEntityId(int entityId) {
        packet.setField(ClientboundRotateHeadPacketAccessor.getFieldEntityId(), entityId);
        return this;
    }

    @Override
    public SClientboundRotateHeadPacket setRotation(byte rotation) {
        packet.setField(ClientboundRotateHeadPacketAccessor.getFieldYHeadRot(), rotation);
        return this;
    }
}
