package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutEntityHeadRotation;

public class BukkitSPacketPlayOutEntityHeadRotation extends BukkitSPacket implements SPacketPlayOutEntityHeadRotation {
    public BukkitSPacketPlayOutEntityHeadRotation() {
        super(ClassStorage.NMS.PacketPlayOutEntityHeadRotation);
    }

    @Override
    public void setEntityId(int entityId) {
        packet.setField("a", entityId);
    }

    @Override
    public void setRotation(byte rotation) {
        packet.setField("b", rotation);
    }
}
