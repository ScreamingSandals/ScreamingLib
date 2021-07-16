package org.screamingsandals.lib.bukkit.packet;
import org.screamingsandals.lib.nms.accessors.ClientboundAnimatePacketAccessor;
import org.screamingsandals.lib.packet.SClientboundAnimatePacket;

public class BukkitSClientboundAnimatePacket extends BukkitSPacket implements SClientboundAnimatePacket {

    public BukkitSClientboundAnimatePacket() {
        super(ClientboundAnimatePacketAccessor.getType());
    }

    @Override
    public SClientboundAnimatePacket setEntityId(int entityId) {
        packet.setField(ClientboundAnimatePacketAccessor.getFieldId(), entityId);
        return this;
    }

    @Override
    public SClientboundAnimatePacket setAnimation(int animationId) {
        packet.setField(ClientboundAnimatePacketAccessor.getFieldAction(), animationId);
        return this;
    }
}
