package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.entity.BukkitDataWatcher;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.nms.accessors.ClientboundSetEntityDataPacketAccessor;
import org.screamingsandals.lib.packet.SClientboundSetEntityDataPacket;
import org.screamingsandals.lib.entity.DataWatcher;
import org.screamingsandals.lib.utils.reflect.Reflect;

public class BukkitSClientboundSetEntityDataPacket extends BukkitSPacket implements SClientboundSetEntityDataPacket {

    public BukkitSClientboundSetEntityDataPacket() {
        super(ClassStorage.NMS.PacketPlayOutEntityMetadata);
    }

    @Override
    public SClientboundSetEntityDataPacket setMetaData(int entityId, DataWatcher dataWatcher, boolean flag) {
        if (!(dataWatcher instanceof BukkitDataWatcher)) {
            throw new UnsupportedOperationException("Invalid data watcher provided!");
        }
        packet = Reflect.constructResulted(ClientboundSetEntityDataPacketAccessor.getConstructor0(), entityId, ((BukkitDataWatcher)dataWatcher).toNMS(), flag);
        return this;
    }
}
