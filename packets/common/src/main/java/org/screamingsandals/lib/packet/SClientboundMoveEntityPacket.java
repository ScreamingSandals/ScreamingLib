package org.screamingsandals.lib.packet;

import org.screamingsandals.lib.utils.math.Vector3D;

public interface SClientboundMoveEntityPacket extends SPacket {

    SClientboundMoveEntityPacket setEntityId(int entityId);

    SClientboundMoveEntityPacket setVelocity(Vector3D velocity);

    SClientboundMoveEntityPacket setYaw(byte yaw);

    SClientboundMoveEntityPacket setPitch(byte pitch);

    SClientboundMoveEntityPacket setIsOnGround(boolean isOnGround);

    SClientboundMoveEntityPacket setHasRotation(boolean hasRotation);

    SClientboundMoveEntityPacket setHasPosition(boolean hasPosition);

    // PacketPlayOutEntityLook
    interface Rot extends SPacket {

        SClientboundMoveEntityPacket.Rot setEntityId(int entityId);

        SClientboundMoveEntityPacket.Rot setYaw(byte yaw);

        SClientboundMoveEntityPacket.Rot setPitch(byte pitch);

        SClientboundMoveEntityPacket.Rot setOnGround(boolean isOnGround);
    }
}
