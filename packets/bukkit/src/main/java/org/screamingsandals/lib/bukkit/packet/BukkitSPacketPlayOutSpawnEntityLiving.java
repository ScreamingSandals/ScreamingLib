package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.entity.BukkitDataWatcher;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutSpawnEntityLiving;
import org.screamingsandals.lib.entity.DataWatcher;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.world.LocationHolder;
import java.util.UUID;

public class BukkitSPacketPlayOutSpawnEntityLiving extends BukkitSPacket implements SPacketPlayOutSpawnEntityLiving {
    public BukkitSPacketPlayOutSpawnEntityLiving() {
        super(ClassStorage.NMS.PacketPlayOutSpawnEntityLiving);
    }

    @Override
    public void setEntityId(int entityId) {
        packet.setField("a,field_149042_a", entityId);
    }

    @Override
    public void setUUID(UUID uuid) {
        if (uuid == null) {
            throw new UnsupportedOperationException("UUID cannot be null!");
        }
        packet.setField("b,field_186894_b", uuid);
    }

    @Override
    public void setType(int entityType) {
        packet.setField("c,field_149040_b", entityType);
    }

    @Override
    public void setLocation(LocationHolder location) {
        if (location == null) {
            throw new UnsupportedOperationException("Location cannot be null!");
        }
        packet.setField("d,field_149041_c", location.getX());
        packet.setField("e,field_149038_d", location.getY());
        packet.setField("f,field_149039_e", location.getZ());
    }

    @Override
    public void setVelocity(Vector3D velocity) {
        if (velocity == null) {
            throw new UnsupportedOperationException("Velocity cannot be null!");
        }
        packet.setField("g,field_149036_f", (int) (velocity.getX() * 8000.0D));
        packet.setField("h,field_149037_g", (int) (velocity.getY() * 8000.0D));
        packet.setField("i,field_149047_h", (int) (velocity.getZ() * 8000.0D));
    }

    @Override
    public void setYaw(float yaw) {
        packet.setField("k,field_149045_j", (byte) (yaw * 256.0F / 300.0F));
    }

    @Override
    public void setPitch(float pitch) {
        packet.setField("j,field_149048_i", (byte) pitch * 256.0F / 300.0F);
    }

    @Override
    public void setHeadPitch(float headPitch) {
        packet.setField("l,field_149046_k", (byte) (headPitch * 256.0F / 300.0F));
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
