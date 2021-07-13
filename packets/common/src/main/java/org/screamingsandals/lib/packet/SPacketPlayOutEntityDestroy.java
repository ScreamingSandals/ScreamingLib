package org.screamingsandals.lib.packet;

public interface SPacketPlayOutEntityDestroy extends SPacket {

    SPacketPlayOutEntityDestroy setEntitiesToDestroy(int[] entityIdArray);

    SPacketPlayOutEntityDestroy setEntityToDestroy(int entityId);
}
