package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutEntityStatus;

public class BukkitSPacketPlayOutEntityStatus extends BukkitSPacket implements SPacketPlayOutEntityStatus {

    public BukkitSPacketPlayOutEntityStatus() {
        super(ClassStorage.NMS.PacketPlayOutEntityStatus);
    }

    @Override
    public SPacketPlayOutEntityStatus setStatus(byte status) {
        packet.setField("b,field_149163_b,f_132089_", status);
        return this;
    }

    @Override
    public SPacketPlayOutEntityStatus setEntityId(int id) {
        packet.setField("a,field_149164_a,f_132088_", id);
        return this;
    }
}
