package org.screamingsandals.lib.packet;

public interface SClientboundSetCarriedItemPacket extends SPacket {

    SClientboundSetCarriedItemPacket setSlot(int slot);
}
