package org.screamingsandals.lib.bukkit;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.common.SPacketPlayOutSpawnEntityLiving;
import org.screamingsandals.lib.entity.EntityLiving;
import org.screamingsandals.lib.utils.reflect.Reflect;

public class BukkitSPacketPlayOutSpawnEntityLiving extends BukkitSPacket implements SPacketPlayOutSpawnEntityLiving {

    public BukkitSPacketPlayOutSpawnEntityLiving() {
        super(ClassStorage.NMS.PacketPlayOutSpawnEntityLiving);
    }

    @Override
    public void setEntity(EntityLiving entity) {
        final var bukkitEntity = entity.as(EntityLiving.class);
        final var handle = ClassStorage.getHandle(bukkitEntity);
        packet = Reflect.constructor(ClassStorage.NMS.PacketPlayOutSpawnEntityLiving, ClassStorage.NMS.EntityLiving)
                .constructResulted(handle);

        if (Version.isVersion(1, 15)) {
            final var dataWatcher = ClassStorage.getDataWatcher(handle);
            final var metadataPacket = Reflect
                    .constructor(ClassStorage.NMS.PacketPlayOutEntityMetadata, int.class, ClassStorage.NMS.DataWatcher, boolean.class)
                    .constructResulted(entity.getEntityId(), dataWatcher, true);
            addAdditionalPacket(metadataPacket);
        }
    }
}
