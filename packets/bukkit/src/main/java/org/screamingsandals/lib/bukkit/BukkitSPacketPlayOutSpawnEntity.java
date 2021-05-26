package org.screamingsandals.lib.bukkit;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.common.SPacketPlayOutSpawnEntity;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.UUID;

public class BukkitSPacketPlayOutSpawnEntity extends BukkitSPacket implements SPacketPlayOutSpawnEntity {
    public BukkitSPacketPlayOutSpawnEntity() {
        super(ClassStorage.NMS.PacketPlayOutSpawnEntity);
    }

    @Override
    public void setEntityId(int entityId) {
        packet.setField("a", entityId);
    }

    @Override
    public void setUUID(UUID uuid) {
        if (uuid == null) {
            throw new UnsupportedOperationException("UUID cannot be null!");
        }
        packet.setField("b", uuid);
    }

    @Override
    public void setLocation(LocationHolder location) {
        if (location == null) {
            throw new UnsupportedOperationException("Location cannot be null!");
        }
        packet.setField("c", location.getX());
        packet.setField("d", location.getY());
        packet.setField("e", location.getZ());

        packet.setField("i", (byte) (location.getYaw() * 256.0F / 300.0F));
        packet.setField("j", (byte) (location.getPitch() * 256.0F / 300.0F));
    }

    @Override
    public void setVelocity(Vector3D velocity) {
        if (velocity == null) {
            throw new UnsupportedOperationException("Velocity cannot be null!");
        }
        packet.setField("f", (int) (velocity.getX() * 8000.0D));
        packet.setField("g", (int) (velocity.getY() * 8000.0D));
        packet.setField("h", (int) (velocity.getZ() * 8000.0D));
    }

    @Override
    public void setType(int typeId) {
        packet.setField("k", typeId);
    }

    @Override
    public void setObjectData(int objectData) {
        packet.setField("l", objectData);
    }
}
