package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.nms.accessors.ClientboundTeleportEntityPacketAccessor;
import org.screamingsandals.lib.packet.SClientboundTeleportEntityPacket;
import org.screamingsandals.lib.world.LocationHolder;

public class BukkitSClientboundTeleportEntityPacket extends BukkitSPacket implements SClientboundTeleportEntityPacket {

    public BukkitSClientboundTeleportEntityPacket() {
        super(ClientboundTeleportEntityPacketAccessor.getType());
    }

    @Override
    public SClientboundTeleportEntityPacket setEntityId(int entityId) {
        packet.setField(ClientboundTeleportEntityPacketAccessor.getFieldId(), entityId);
        return this;
    }

    @Override
    public SClientboundTeleportEntityPacket setLocation(LocationHolder location) {
        if (location == null) {
            throw new UnsupportedOperationException("Location cannot be null!");
        }

        packet.setField(ClientboundTeleportEntityPacketAccessor.getFieldX(), location.getX());
        packet.setField(ClientboundTeleportEntityPacketAccessor.getFieldY(), location.getY());
        packet.setField(ClientboundTeleportEntityPacketAccessor.getFieldZ(), location.getZ());
        packet.setField(ClientboundTeleportEntityPacketAccessor.getFieldYRot(), (byte) ((int) (location.getYaw() * 256.0F / 360.0F)));
        packet.setField(ClientboundTeleportEntityPacketAccessor.getFieldXRot(), (byte) ((int) (location.getPitch() * 256.0F / 360.0F)));
        return this;
    }

    @Override
    public SClientboundTeleportEntityPacket setIsOnGround(boolean isOnGround) {
        packet.setField(ClientboundTeleportEntityPacketAccessor.getFieldOnGround(), isOnGround);
        return this;
    }
}
