package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.common.packet.SPacketPlayOutCollect;

public class BukkitSPacketPlayOutCollect extends BukkitSPacket implements SPacketPlayOutCollect {
    public BukkitSPacketPlayOutCollect() {
        super(ClassStorage.NMS.PacketPlayOutCollect);
    }

    @Override
    public void setEntityId(int entityId) {
        packet.setField("a", entityId);
    }

    @Override
    public void setCollectedEntityId(int collectedEntityId) {
        packet.setField("b", collectedEntityId);
    }
}
