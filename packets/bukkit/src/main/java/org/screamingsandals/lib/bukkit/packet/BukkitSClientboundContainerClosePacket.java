package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.nms.accessors.ClientboundContainerClosePacketAccessor;
import org.screamingsandals.lib.packet.SClientboundContainerClosePacket;

public class BukkitSClientboundContainerClosePacket extends BukkitSPacket implements SClientboundContainerClosePacket {

    public BukkitSClientboundContainerClosePacket() {
        super(ClientboundContainerClosePacketAccessor.getType());
    }

    @Override
    public SClientboundContainerClosePacket setWindowId(int windowId) {
        packet.setField(ClientboundContainerClosePacketAccessor.getFieldContainerId(), windowId);
        return this;
    }
}
