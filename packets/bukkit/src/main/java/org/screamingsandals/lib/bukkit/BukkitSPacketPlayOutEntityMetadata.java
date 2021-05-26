package org.screamingsandals.lib.bukkit;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.bukkit.utils.nms.entity.BukkitDataWatcher;
import org.screamingsandals.lib.common.SPacketPlayOutEntityMetadata;
import org.screamingsandals.lib.utils.entity.DataWatcher;
import org.screamingsandals.lib.utils.reflect.Reflect;

public class BukkitSPacketPlayOutEntityMetadata extends BukkitSPacket implements SPacketPlayOutEntityMetadata {
    public BukkitSPacketPlayOutEntityMetadata() {
        super(ClassStorage.NMS.PacketPlayOutEntityMetadata);
    }

    @Override
    public void setMetaData(int entityId, DataWatcher dataWatcher, boolean flag) {
        if (dataWatcher == null) {
            throw new UnsupportedOperationException("Invalid data watcher provided!");
        }
       packet = Reflect
                .constructor(ClassStorage.NMS.PacketPlayOutEntityMetadata, int.class, ClassStorage.NMS.DataWatcher, boolean.class)
                .constructResulted(entityId, ((BukkitDataWatcher)dataWatcher).toNMS(), flag);
    }
}
