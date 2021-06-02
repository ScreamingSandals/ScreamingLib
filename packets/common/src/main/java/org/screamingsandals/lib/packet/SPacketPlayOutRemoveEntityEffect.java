package org.screamingsandals.lib.packet;

public interface SPacketPlayOutRemoveEntityEffect extends SPacket {
    void setEntityId(int entityId);

    void setEffect(int effect);
}
