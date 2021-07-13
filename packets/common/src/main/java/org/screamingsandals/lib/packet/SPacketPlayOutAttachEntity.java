package org.screamingsandals.lib.packet;

public interface SPacketPlayOutAttachEntity extends SPacket {

    SPacketPlayOutAttachEntity setEntityId(int entityId);

    SPacketPlayOutAttachEntity setHoldingEntityId(int entityId);
}
