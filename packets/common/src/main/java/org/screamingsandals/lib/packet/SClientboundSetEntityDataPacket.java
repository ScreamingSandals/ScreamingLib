package org.screamingsandals.lib.packet;


import org.screamingsandals.lib.entity.DataWatcher;

public interface SClientboundSetEntityDataPacket extends SPacket{

    SClientboundSetEntityDataPacket setMetaData(int entityId, DataWatcher dataWatcher, boolean flag);
}
