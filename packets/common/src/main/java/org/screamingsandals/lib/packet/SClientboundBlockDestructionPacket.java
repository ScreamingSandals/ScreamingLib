package org.screamingsandals.lib.packet;

import org.screamingsandals.lib.world.LocationHolder;

public interface SClientboundBlockDestructionPacket extends SPacket {

    SClientboundBlockDestructionPacket setEntityId(int entityId);

    SClientboundBlockDestructionPacket setBlockLocation(LocationHolder blockLocation);

    SClientboundBlockDestructionPacket setDestroyStage(int destroyStage);
}
