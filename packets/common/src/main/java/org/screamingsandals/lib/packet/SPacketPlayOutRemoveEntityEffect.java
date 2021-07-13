package org.screamingsandals.lib.packet;

public interface SPacketPlayOutRemoveEntityEffect extends SPacket {

    SPacketPlayOutRemoveEntityEffect setEntityId(int entityId);

    SPacketPlayOutRemoveEntityEffect setEffect(int effect);
}
