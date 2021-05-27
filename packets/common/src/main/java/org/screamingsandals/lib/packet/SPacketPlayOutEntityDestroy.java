package org.screamingsandals.lib.packet;

public interface SPacketPlayOutEntityDestroy extends SPacket {
    void setEntitiesToDestroy(int[] entityIdArray);
}
