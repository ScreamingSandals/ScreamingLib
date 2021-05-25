package org.screamingsandals.lib.bukkit;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.common.SPacketPlayOutSpawnEntity;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.utils.reflect.Reflect;
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

        packet.setField("i", location.getYaw());
        packet.setField("j", location.getPitch());
    }

    @Override
    public void setVelocity(Vector3D velocity) {
        if (velocity == null) {
            throw new UnsupportedOperationException("Velocity cannot be null!");
        }
        packet.setField("f", velocity.getX());
        packet.setField("g", velocity.getY());
        packet.setField("h", velocity.getZ());
    }

    @Override
    public void setType(int typeId) {
        packet.setField("k", typeId);
    }

    @Override
    public void setObjectData(int objectData) {
        packet.setField("l", objectData);
    }

    @Override
    public void setDataWatcher(Object dataWatcher, int entityId) {
        if (dataWatcher == null) {
            throw new UnsupportedOperationException("DataWatcher cannot be null!");
        }
        if (Version.isVersion(1, 15)) {
            final var metadataPacket = Reflect
                    .constructor(ClassStorage.NMS.PacketPlayOutEntityMetadata, int.class, ClassStorage.NMS.DataWatcher, boolean.class)
                    .constructResulted(entityId, dataWatcher, true);
            addAdditionalPacket(metadataPacket);
        }
    }
}
