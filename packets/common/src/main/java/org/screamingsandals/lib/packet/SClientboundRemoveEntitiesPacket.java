package org.screamingsandals.lib.packet;

public interface SClientboundRemoveEntitiesPacket extends SPacket {

    SClientboundRemoveEntitiesPacket setEntitiesToDestroy(int[] entityIdArray);

    SClientboundRemoveEntitiesPacket setEntityToDestroy(int entityId);
}
