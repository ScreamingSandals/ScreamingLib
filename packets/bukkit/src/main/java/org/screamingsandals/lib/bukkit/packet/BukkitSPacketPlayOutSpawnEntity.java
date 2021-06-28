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
    public void setEntityId(int entityId) {
        if (Version.isVersion(1, 17)) {
            packet.setField("c", entityId);
        } else {
            packet.setField("a,field_149018_a", entityId);
        }
    }

    @Override
    public void setUUID(UUID uuid) {
        if (uuid == null) {
            throw new UnsupportedOperationException("UUID cannot be null!");
        }
        if (Version.isVersion(1, 17)) {
            packet.setField("d", uuid);
        } else {
            packet.setField("b,field_186883_b", uuid);
        }
    }

    @Override
    public void setLocation(LocationHolder location) {
        if (location == null) {
            throw new UnsupportedOperationException("Location cannot be null!");
        }
        if (Version.isVersion(1, 17)) {
            packet.setField("e", location.getX());
            packet.setField("f", location.getY());
            packet.setField("g", location.getZ());
            packet.setField("k", (byte) (location.getYaw() * 256.0F / 300.0F));
            packet.setField("l", (byte) (location.getPitch() * 256.0F / 300.0F));
        } else {
            packet.setField("c,field_149016_b", location.getX());
            packet.setField("d,field_149017_c", location.getY());
            packet.setField("e,field_149014_d", location.getZ());
            packet.setField("i,field_149021_h", (byte) (location.getYaw() * 256.0F / 300.0F));
            packet.setField("j,field_149022_i", (byte) (location.getPitch() * 256.0F / 300.0F));
        }
    }

    @Override
    public void setVelocity(Vector3D velocity) {
        if (velocity == null) {
            throw new UnsupportedOperationException("Velocity cannot be null!");
        }
        if (Version.isVersion(1, 17)) {
            packet.setField("h", (int) (velocity.getX() * 8000.0D));
            packet.setField("i", (int) (velocity.getY() * 8000.0D));
            packet.setField("j", (int) (velocity.getZ() * 8000.0D));
        } else {
            packet.setField("f,field_149015_e", (int) (velocity.getX() * 8000.0D));
            packet.setField("g,field_149012_f", (int) (velocity.getY() * 8000.0D));
            packet.setField("h,field_149013_g", (int) (velocity.getZ() * 8000.0D));
        }
    }

    @Override
    public void setType(int typeId) {
        if (Version.isVersion(1, 17)) {
            final var field = Reflect.getField(ClassStorage.NMS.IRegistry, "ENTITY_TYPE,field_212629_r,Y");
            final var entityTypeNMS = Reflect.getMethod(field, "fromId,func_148745_a", int.class)
                    .invoke(typeId);
            packet.setField("m", entityTypeNMS);
        } else {
            packet.setField("k,field_149019_j", typeId);
        }
    }

    @Override
    public void setObjectData(int objectData) {
        if (Version.isVersion(1, 17)) {
            packet.setField("n", objectData);
        } else {
            packet.setField("l,field_149020_k", objectData);
        }
    }
}
