package org.screamingsandals.lib.common;

public interface SPacketPlayOutAttachEntity extends SPacket {
    void setEntityId(int entityId);

    void setHoldingEntityId(int entityId);
}
