package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.nms.accessors.ClientboundTakeItemEntityPacketAccessor;
import org.screamingsandals.lib.packet.SClientboundTakeItemEntityPacket;

public class BukkitSClientboundTakeItemEntityPacket extends BukkitSPacket implements SClientboundTakeItemEntityPacket {

    public BukkitSClientboundTakeItemEntityPacket() {
        super(ClientboundTakeItemEntityPacketAccessor.getType());
    }

    @Override
    public SClientboundTakeItemEntityPacket setItemId(int entityId) {
        packet.setField(ClientboundTakeItemEntityPacketAccessor.getFieldItemId(), entityId);
        return this;
    }

    @Override
    public SClientboundTakeItemEntityPacket setCollectedEntityId(int collectedEntityId) {
        packet.setField(ClientboundTakeItemEntityPacketAccessor.getFieldPlayerId(), collectedEntityId);
        return this;
    }

    @Override
    public SClientboundTakeItemEntityPacket setAmount(int amount) {
        packet.setField(ClientboundTakeItemEntityPacketAccessor.getFieldAmount(), amount);
        return this;
    }
}
