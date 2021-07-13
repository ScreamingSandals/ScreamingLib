package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.packet.SPacketPlayOutSpawnEntity;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.LocationHolder;

import java.sql.Ref;
import java.util.UUID;

public class BukkitSPacketPlayOutSpawnEntity extends BukkitSPacket implements SPacketPlayOutSpawnEntity {

    public BukkitSPacketPlayOutSpawnEntity() {
        super(ClassStorage.NMS.PacketPlayOutSpawnEntity);
    }

    @Override
    public SPacketPlayOutSpawnEntity setEntityId(int entityId) {
        if (Version.isVersion(1, 17)) {
            packet.setField("c,f_131456_", entityId);
        } else {
            packet.setField("a,field_149018_a", entityId);
        }
        return this;
    }

    @Override
    public SPacketPlayOutSpawnEntity setUUID(UUID uuid) {
        if (uuid == null) {
            throw new UnsupportedOperationException("UUID cannot be null!");
        }

        if (Version.isVersion(1, 17)) {
            packet.setField("d,f_131457_", uuid);
        } else {
            packet.setField("b,field_186883_b", uuid);
        }
        return this;
    }

    @Override
    public SPacketPlayOutSpawnEntity setLocation(LocationHolder location) {
        if (location == null) {
            throw new UnsupportedOperationException("Location cannot be null!");
        }

        if (Version.isVersion(1, 17)) {
            packet.setField("e,f_131458_", location.getX());
            packet.setField("f,f_131459_", location.getY());
            packet.setField("g,f_131460_", location.getZ());
            packet.setField("l,f_131465_", (byte) (location.getYaw() * 256.0F / 300.0F));
            packet.setField("k,f_131464_", (byte) (location.getPitch() * 256.0F / 300.0F));
        } else {
            packet.setField("c,field_149016_b", location.getX());
            packet.setField("d,field_149017_c", location.getY());
            packet.setField("e,field_149014_d", location.getZ());
            packet.setField("j,field_149021_h", (byte) (location.getYaw() * 256.0F / 300.0F));
            packet.setField("i,field_149022_i", (byte) (location.getPitch() * 256.0F / 300.0F));
        }
        return this;
    }

    @Override
    public SPacketPlayOutSpawnEntity setVelocity(Vector3D velocity) {
        if (velocity == null) {
            throw new UnsupportedOperationException("Velocity cannot be null!");
        }
        if (Version.isVersion(1, 17)) {
            packet.setField("h,f_131461_", (int) (velocity.getX() * 8000.0D));
            packet.setField("i,f_131462_", (int) (velocity.getY() * 8000.0D));
            packet.setField("j,f_131463_", (int) (velocity.getZ() * 8000.0D));
        } else {
            packet.setField("f,field_149015_e", (int) (velocity.getX() * 8000.0D));
            packet.setField("g,field_149012_f", (int) (velocity.getY() * 8000.0D));
            packet.setField("h,field_149013_g", (int) (velocity.getZ() * 8000.0D));
        }
        return this;
    }

    @Override
    public SPacketPlayOutSpawnEntity setType(int typeId) {
        if (Version.isVersion(1, 17)) {
            final var field = Reflect.getField(ClassStorage.NMS.IRegistry, "ENTITY_TYPE,field_212629_r,Y,f_122826_");
            final var entityTypeNMS = Reflect.getMethod(field, "fromId,func_148745_a,m_7942_", int.class).invoke(typeId);
            packet.setField("m,f_131466_", entityTypeNMS);
        } else {
            packet.setField("k,field_149019_j", typeId);
        }
        return this;
    }

    @Override
    public SPacketPlayOutSpawnEntity setObjectData(int objectData) {
        if (Version.isVersion(1, 17)) {
            packet.setField("n,f_131467_", objectData);
        } else {
            packet.setField("l,field_149020_k", objectData);
        }
        return this;
    }
}
