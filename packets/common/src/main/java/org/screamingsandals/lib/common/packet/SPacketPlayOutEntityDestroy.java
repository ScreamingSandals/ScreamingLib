package org.screamingsandals.lib.common.packet;

public interface SPacketPlayOutEntityDestroy extends SPacket {
    void setEntitiesToDestroy(int[] entityIdArray);
}
