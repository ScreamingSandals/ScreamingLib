package org.screamingsandals.lib.bukkit;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.common.SPacketPlayOutKeepAlive;

public class BukkitSPacketPlayOutKeepAlive extends BukkitSPacket implements SPacketPlayOutKeepAlive {
    public BukkitSPacketPlayOutKeepAlive() {
        super(ClassStorage.NMS.PacketPlayOutKeepAlive);
    }

    @Override
    public void setEntityId(int entityId) {
        packet.setField("a", entityId);
    }
}
