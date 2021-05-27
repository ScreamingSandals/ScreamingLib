package org.screamingsandals.lib.common.packet;

import org.screamingsandals.lib.world.LocationHolder;

public interface SPacketPlayOutBlockBreakAnimation extends SPacket{
    void setEntityId(int entityId);

    void setBlockLocation(LocationHolder blockLocation);

    void setDestroyStage(int destroyStage);
}
