package org.screamingsandals.lib.bukkit;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.bukkit.utils.nms.entity.BukkitDataWatcher;
import org.screamingsandals.lib.common.SPacketPlayOutSpawnEntityLiving;
import org.screamingsandals.lib.utils.entity.DataWatcher;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.world.LocationHolder;
import java.util.UUID;

public class BukkitSPacketPlayOutSpawnEntityLiving extends BukkitSPacket implements SPacketPlayOutSpawnEntityLiving {
    public BukkitSPacketPlayOutSpawnEntityLiving() {
        super(ClassStorage.NMS.PacketPlayOutSpawnEntityLiving);
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
    public void setType(int entityType) {
        packet.setField("c", entityType);
    }

    @Override
    public void setLocation(LocationHolder location) {
        if (location == null) {
            throw new UnsupportedOperationException("Location cannot be null!");
        }
        packet.setField("d", location.getX());
        packet.setField("e", location.getY());
        packet.setField("f", location.getZ());
    }

    @Override
    public void setVelocity(Vector3D velocity) {
        if (velocity == null) {
            throw new UnsupportedOperationException("Velocity cannot be null!");
        }
        packet.setField("g", (int) (velocity.getX() * 8000.0D));
        packet.setField("h", (int) (velocity.getY() * 8000.0D));
        packet.setField("i", (int) (velocity.getZ() * 8000.0D));
    }

    @Override
    public void setYaw(float yaw) {
        packet.setField("j", (byte) yaw * 256.0F / 300.0F);
    }

    @Override
    public void setPitch(float pitch) {
        packet.setField("k", (byte) (pitch * 256.0F / 300.0F));
    }

    @Override
    public void setHeadPitch(float headPitch) {
        packet.setField("l", (byte) (headPitch * 256.0F / 300.0F));
    }

    @Override
    public void setDataWatcher(DataWatcher dataWatcher) {
        if (dataWatcher == null) {
            throw new UnsupportedOperationException("DataWatcher cannot be null!");
        }
        if (!(dataWatcher instanceof BukkitDataWatcher)) {
            throw new UnsupportedOperationException("DataWatcher is not an instance of BukkitDataWatcher!");
        }
        final var bukkitDataWatcher = (BukkitDataWatcher) dataWatcher;
        final var nmsDataWatcher = bukkitDataWatcher.toNMS();
        packet.setField("m", nmsDataWatcher);
    }
}
