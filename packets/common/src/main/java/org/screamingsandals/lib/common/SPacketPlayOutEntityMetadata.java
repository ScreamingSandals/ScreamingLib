package org.screamingsandals.lib.common;


import org.screamingsandals.lib.entity.DataWatcher;

public interface SPacketPlayOutEntityMetadata extends SPacket{
    void setMetaData(int entityId, DataWatcher dataWatcher, boolean flag);
}
