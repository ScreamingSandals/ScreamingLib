package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.entity.BukkitDataWatcher;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.entity.DataWatcher;
import org.screamingsandals.lib.nms.accessors.ClientboundAddMobPacketAccessor;
import org.screamingsandals.lib.packet.SClientboundAddMobPacket;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.UUID;

public class BukkitSClientboundAddMobPacket extends BukkitSPacket implements SClientboundAddMobPacket {

    public BukkitSClientboundAddMobPacket() {
        super(ClientboundAddMobPacketAccessor.getType());
    }

    @Override
    public SClientboundAddMobPacket setEntityId(int entityId) {
        packet.setField(ClientboundAddMobPacketAccessor.getFieldId(), entityId);
        return this;
    }

    @Override
    public SClientboundAddMobPacket setUUID(UUID uuid) {
        if (uuid == null) {
            throw new UnsupportedOperationException("UUID cannot be null!");
        }

        packet.setField(ClientboundAddMobPacketAccessor.getFieldUuid(), uuid);
        return this;
    }

    @Override
    public SClientboundAddMobPacket setLocation(LocationHolder location) {
        if (location == null) {
            throw new UnsupportedOperationException("Location cannot be null!");
        }

        packet.setField(ClientboundAddMobPacketAccessor.getFieldX(), location.getX());
        packet.setField(ClientboundAddMobPacketAccessor.getFieldY(), location.getY());
        packet.setField(ClientboundAddMobPacketAccessor.getFieldZ(), location.getZ());

        packet.setField(ClientboundAddMobPacketAccessor.getFieldYRot(), (byte) (location.getYaw() * 256.0F / 300.0F));
        packet.setField(ClientboundAddMobPacketAccessor.getFieldXRot(), (byte) (location.getPitch() * 256.0F / 300.0F));
        return this;
    }

    @Override
    public SClientboundAddMobPacket setVelocity(Vector3D velocity) {
        if (velocity == null) {
            velocity = new Vector3D(0, 0, 0);
        }

        packet.setField(ClientboundAddMobPacketAccessor.getFieldXd(), (int) (velocity.getX() * 8000.0D));
        packet.setField(ClientboundAddMobPacketAccessor.getFieldYd(), (int) (velocity.getY() * 8000.0D));
        packet.setField(ClientboundAddMobPacketAccessor.getFieldZd(), (int) (velocity.getZ() * 8000.0D));
        return this;
    }

    @Override
    public SClientboundAddMobPacket setType(int typeId) {
        packet.setField(ClientboundAddMobPacketAccessor.getFieldType(), typeId);
        return this;
    }

    @Override
    public SClientboundAddMobPacket setDataWatcher(DataWatcher dataWatcher) {
        if (!(dataWatcher instanceof BukkitDataWatcher)) {
            throw new UnsupportedOperationException("Invalid DtaWatcher provided!");
        }

        packet.setField(ClientboundAddMobPacketAccessor.getFieldField_149043_l(), ((BukkitDataWatcher) dataWatcher).toNMS());
        return this;
    }

    @Override
    public SClientboundAddMobPacket setHeadYaw(byte yaw) {
        packet.setField(ClientboundAddMobPacketAccessor.getFieldYHeadRot(), yaw);
        return this;
    }
}
