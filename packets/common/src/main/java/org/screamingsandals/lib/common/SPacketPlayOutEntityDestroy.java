package org.screamingsandals.lib.common;

public interface SPacketPlayOutEntityDestroy extends SPacket {
    void setEntitiesToDestroy(int[] entityIdArray);
}
