package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.nms.accessors.ClientboundAddEntityPacketAccessor;
import org.screamingsandals.lib.packet.SClientboundAddEntityPacket;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.UUID;

public class BukkitSClientboundAddEntityPacket extends BukkitSPacket implements SClientboundAddEntityPacket {

    public BukkitSClientboundAddEntityPacket() {
        super(ClientboundAddEntityPacketAccessor.getType());
    }

    @Override
    public SClientboundAddEntityPacket setEntityId(int id) {
        packet.setField(ClientboundAddEntityPacketAccessor.getFieldId(), id);
        return this;
    }

    @Override
    public SClientboundAddEntityPacket setUUID(UUID uuid) {
        packet.setField(ClientboundAddEntityPacketAccessor.getFieldUuid(), uuid);
        return this;
    }

    @Override
    public SClientboundAddEntityPacket setLocation(LocationHolder location) {
        if (location == null) {
            throw new UnsupportedOperationException("Location cannot be null!");
        }

        packet.setField(ClientboundAddEntityPacketAccessor.getFieldX(), location.getX());
        packet.setField(ClientboundAddEntityPacketAccessor.getFieldY(), location.getY());
        packet.setField(ClientboundAddEntityPacketAccessor.getFieldZ(), location.getZ());

        packet.setField(ClientboundAddEntityPacketAccessor.getFieldYRot(), (byte) (location.getYaw() * 256.0F / 300.0F));
        packet.setField(ClientboundAddEntityPacketAccessor.getFieldXRot(), (byte) (location.getPitch() * 256.0F / 300.0F));
        return this;
    }

    @Override
    public SClientboundAddEntityPacket setVelocity(Vector3D velocity) {
        if (velocity == null) {
            velocity = new Vector3D(0, 0, 0);
        }

        packet.setField(ClientboundAddEntityPacketAccessor.getFieldXa(), (int) (velocity.getX() * 8000.0D));
        packet.setField(ClientboundAddEntityPacketAccessor.getFieldYa(), (int) (velocity.getY() * 8000.0D));
        packet.setField(ClientboundAddEntityPacketAccessor.getFieldZa(), (int) (velocity.getZ() * 8000.0D));
        return this;
    }

    @Override
    public SClientboundAddEntityPacket setType(int typeId) {
        packet.setField(ClientboundAddEntityPacketAccessor.getFieldType(), typeId);
        return this;
    }

    @Override
    public SClientboundAddEntityPacket setObjectData(int data) {
        packet.setField(ClientboundAddEntityPacketAccessor.getFieldData(), data);
        return this;
    }
}
