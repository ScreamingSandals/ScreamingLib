package org.screamingsandals.lib.packet;


import org.screamingsandals.lib.entity.DataWatcher;

public interface SPacketPlayOutEntityMetadata extends SPacket{

    SPacketPlayOutEntityMetadata setMetaData(int entityId, DataWatcher dataWatcher, boolean flag);
}
