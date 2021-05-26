package org.screamingsandals.lib.common;

public interface SPacketPlayOutRemoveEntityEffect extends SPacket {
    void setEntityId(int entityId);

    void setEffect(int effect);
}
