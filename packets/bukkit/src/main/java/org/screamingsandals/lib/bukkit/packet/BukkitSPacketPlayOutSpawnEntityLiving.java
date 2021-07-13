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
    public SPacketPlayOutSpawnEntityLiving setEntityId(int entityId) {
        packet.setField("a,field_149042_a,f_131531_", entityId);
        return this;
    }

    @Override
    public SPacketPlayOutSpawnEntityLiving setUUID(UUID uuid) {
        if (uuid == null) {
            throw new UnsupportedOperationException("UUID cannot be null!");
        }

        packet.setField("b,field_186894_b,f_131532_", uuid);
        return this;
    }

    @Override
    public SPacketPlayOutSpawnEntityLiving setType(int entityType) {
        packet.setField("c,field_149040_b,f_131533_", entityType);
        return this;
    }

    @Override
    public SPacketPlayOutSpawnEntityLiving setLocation(LocationHolder location) {
        if (location == null) {
            throw new UnsupportedOperationException("Location cannot be null!");
        }

        packet.setField("d,field_149041_c,f_131534_", location.getX());
        packet.setField("e,field_149038_d,f_131535_", location.getY());
        packet.setField("f,field_149039_e,f_131536_", location.getZ());
        return this;
    }

    @Override
    public SPacketPlayOutSpawnEntityLiving setVelocity(Vector3D velocity) {
        if (velocity == null) {
            throw new UnsupportedOperationException("Velocity cannot be null!");
        }

        packet.setField("g,field_149036_f,f_131537_", (int) (velocity.getX() * 8000.0D));
        packet.setField("h,field_149037_g,f_131538_", (int) (velocity.getY() * 8000.0D));
        packet.setField("i,field_149047_h,f_131539_", (int) (velocity.getZ() * 8000.0D));
        return this;
    }

    @Override
    public SPacketPlayOutSpawnEntityLiving setYaw(float yaw) {
        packet.setField("j,field_149045_j,f_131540_", (byte) (yaw * 256.0F / 300.0F));
        return this;
    }

    @Override
    public SPacketPlayOutSpawnEntityLiving setPitch(float pitch) {
        packet.setField("k,field_149048_i,f_131541_", (byte) pitch * 256.0F / 300.0F);
        return this;
    }

    @Override
    public SPacketPlayOutSpawnEntityLiving setHeadYaw(float headPitch) {
        packet.setField("l,field_149046_k,f_131542", (byte) (headPitch * 256.0F / 300.0F));
        return this;
    }

    @Override
    public SPacketPlayOutSpawnEntityLiving setDataWatcher(DataWatcher dataWatcher) {
        if (dataWatcher == null) {
            throw new UnsupportedOperationException("DataWatcher cannot be null!");
        }

        if (!(dataWatcher instanceof BukkitDataWatcher)) {
            throw new UnsupportedOperationException("DataWatcher is not an instance of BukkitDataWatcher!");
        }

        final var bukkitDataWatcher = (BukkitDataWatcher) dataWatcher;
        final var nmsDataWatcher = bukkitDataWatcher.toNMS();
        packet.setField("m,field_149043_l", nmsDataWatcher);
        return this;
    }
}
