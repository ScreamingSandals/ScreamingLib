package org.screamingsandals.lib.bukkit.packet;
import org.screamingsandals.lib.bukkit.entity.BukkitDataWatcher;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutNamedEntitySpawn;
import org.screamingsandals.lib.entity.DataWatcher;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.UUID;

public class BukkitSPacketPlayOutNamedEntitySpawn extends BukkitSPacket implements SPacketPlayOutNamedEntitySpawn {

    public BukkitSPacketPlayOutNamedEntitySpawn() {
        super(ClassStorage.NMS.PacketPlayOutNamedEntitySpawn);
    }

    @Override
    public SPacketPlayOutNamedEntitySpawn setEntityId(int entityId) {
        packet.setField("a,field_148957_a,f_131587_", entityId);
        return this;
    }

    @Override
    public SPacketPlayOutNamedEntitySpawn setUUID(UUID uuid) {
        if (uuid == null) {
            throw new UnsupportedOperationException("UUID cannot be null!");
        }
        packet.setField("b,field_179820_b,f_131588_", uuid);
        return this;
    }

    @Override
    public SPacketPlayOutNamedEntitySpawn setLocation(LocationHolder location) {
        if (location == null) {
            throw new UnsupportedOperationException("Location cannot be null!");
        }
        packet.setField("c,field_148956_c,f_131589_", location.getX());
        packet.setField("d,field_148953_d,f_131590_", location.getY());
        packet.setField("e,field_148954_e,f_131591_", location.getZ());
        return this;
    }

    @Override
    public SPacketPlayOutNamedEntitySpawn setYaw(float yaw) {
        packet.setField("f,field_148951_f,f_131592_", (byte) (yaw * 256.0F / 360.0F));
        return this;
    }

    @Override
    public SPacketPlayOutNamedEntitySpawn setPitch(float pitch) {
        packet.setField("g,field_148952_g,f_131593_", (byte) (pitch * 256.0F / 360.0F));
        return this;
    }

    @Deprecated
    @Override
    public SPacketPlayOutNamedEntitySpawn setDataWatcher(DataWatcher dataWatcher) {
        if (dataWatcher == null) {
            throw new UnsupportedOperationException("DataWatcher cannot be null!");
        }
        if (!(dataWatcher instanceof BukkitDataWatcher)) {
            throw new UnsupportedOperationException("DataWatcher is not an instance of BukkitDataWatcher!");
        }
        final var bukkitDataWatcher = (BukkitDataWatcher) dataWatcher;
        final var nmsDataWatcher = bukkitDataWatcher.toNMS();
        packet.setField("h,field_148960_i", nmsDataWatcher);
        return this;
    }
}
