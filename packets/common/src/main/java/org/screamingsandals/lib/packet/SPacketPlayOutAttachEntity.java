package org.screamingsandals.lib.packet;

public interface SPacketPlayOutAttachEntity extends SPacket {
    void setEntityId(int entityId);

    void setHoldingEntityId(int entityId);
}
