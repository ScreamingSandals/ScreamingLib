package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutKeepAlive;

public class BukkitSPacketPlayOutKeepAlive extends BukkitSPacket implements SPacketPlayOutKeepAlive {
    public BukkitSPacketPlayOutKeepAlive() {
        super(ClassStorage.NMS.PacketPlayOutKeepAlive);
    }

    @Override
    public void setEntityId(int entityId) {
        packet.setField("a,field_149461_a", entityId);
    }
}
