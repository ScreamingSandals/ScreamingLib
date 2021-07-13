package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutGameStateChange;

//TODO: 1.17 support
public class BukkitSPacketPlayOutGameStateChange extends BukkitSPacket implements SPacketPlayOutGameStateChange {

    public BukkitSPacketPlayOutGameStateChange() {
        super(ClassStorage.NMS.PacketPlayOutGameStateChange);
    }

    @Override
    public SPacketPlayOutGameStateChange setReason(int reason) {
        packet.setField("b,field_149140_b", reason);
        return this;
    }

    @Override
    public SPacketPlayOutGameStateChange setValue(float value) {
        packet.setField("c,field_149141_c", value);
        return this;
    }
}
