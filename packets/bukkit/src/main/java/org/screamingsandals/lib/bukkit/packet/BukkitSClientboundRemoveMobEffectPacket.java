package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.nms.accessors.ClientboundRemoveMobEffectPacketAccessor;
import org.screamingsandals.lib.packet.SClientboundRemoveMobEffectPacket;

public class BukkitSClientboundRemoveMobEffectPacket extends BukkitSPacket implements SClientboundRemoveMobEffectPacket {

    public BukkitSClientboundRemoveMobEffectPacket() {
        super(ClientboundRemoveMobEffectPacketAccessor.getType());
    }

    @Override
    public SClientboundRemoveMobEffectPacket setEntityId(int entityId) {
        packet.setField(ClientboundRemoveMobEffectPacketAccessor.getFieldEntityId(), entityId);
        return this;
    }

    @Override
    public SClientboundRemoveMobEffectPacket setEffect(int effect) {
        packet.setField(ClientboundRemoveMobEffectPacketAccessor.getFieldEffect(), effect);
        return this;
    }
}
