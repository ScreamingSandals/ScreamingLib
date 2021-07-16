package org.screamingsandals.lib.packet;

public interface SClientboundRemoveMobEffectPacket extends SPacket {

    SClientboundRemoveMobEffectPacket setEntityId(int entityId);

    SClientboundRemoveMobEffectPacket setEffect(int effect);
}
