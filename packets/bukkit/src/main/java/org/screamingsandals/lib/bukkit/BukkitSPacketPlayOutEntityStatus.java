package org.screamingsandals.lib.bukkit;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.common.SPacketPlayOutEntityStatus;

public class BukkitSPacketPlayOutEntityStatus extends BukkitSPacket implements SPacketPlayOutEntityStatus {
    public BukkitSPacketPlayOutEntityStatus() {
        super(ClassStorage.NMS.PacketPlayOutEntityStatus);
    }

    @Override
    public void setStatus(byte status) {
        packet.setField("b", status);
    }

    @Override
    public void setEntityId(int id) {
        packet.setField("a", id);
    }
}
