package org.screamingsandals.lib.bukkit.packet;
import org.screamingsandals.lib.bukkit.entity.BukkitDataWatcher;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutNamedEntitySpawn;
import org.screamingsandals.lib.entity.DataWatcher;
import org.screamingsandals.lib.utils.math.Vector3D;

import java.util.UUID;

public class BukkitSPacketPlayOutNamedEntitySpawn extends BukkitSPacket implements SPacketPlayOutNamedEntitySpawn {
    public BukkitSPacketPlayOutNamedEntitySpawn() {
        super(ClassStorage.NMS.PacketPlayOutNamedEntitySpawn);
    }

    @Override
    public void setEntityId(int entityId) {
        packet.setField("a,field_148957_a", entityId);
    }

    @Override
    public void setUUID(UUID uuid) {
        if (uuid == null) {
            throw new UnsupportedOperationException("UUID cannot be null!");
        }
        packet.setField("b,field_179820_b", uuid);
    }

    @Override
    public void setLocation(Vector3D location) {
        if (location == null) {
            throw new UnsupportedOperationException("Location cannot be null!");
        }
        packet.setField("c,field_148956_c", location.getX());
        packet.setField("d,field_148953_d", location.getY());
        packet.setField("e,field_148954_e", location.getZ());
    }

    @Override
    public void setYaw(float yaw) {
        packet.setField("f,field_148951_f", (byte) (yaw * 256.0F / 360.0F));
    }

    @Override
    public void setPitch(float pitch) {
        packet.setField("g,field_148952_g", (byte) (pitch * 256.0F / 360.0F));
    }

    @Deprecated
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
        packet.setField("h", nmsDataWatcher);
    }
}
