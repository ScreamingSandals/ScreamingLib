package org.screamingsandals.lib.bukkit;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.common.SPacketPlayOutNamedEntitySpawn;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.UUID;

public class BukkitSPacketPlayOutNamedEntitySpawn extends BukkitSPacket implements SPacketPlayOutNamedEntitySpawn {
    public BukkitSPacketPlayOutNamedEntitySpawn() {
        super(ClassStorage.NMS.PacketPlayOutNamedEntitySpawn);
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
    public void setLocation(Vector3D location) {
        packet.setField("c", location.getX());
        packet.setField("d", location.getY());
        packet.setField("e", location.getZ());
    }

    @Override
    public void setYaw(float yaw) {
        packet.setField("f", (byte) (yaw * 256.0F / 360.0F));
    }

    @Override
    public void setPitch(float pitch) {
        packet.setField("g", (byte) (pitch * 256.0F / 360.0F));
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
