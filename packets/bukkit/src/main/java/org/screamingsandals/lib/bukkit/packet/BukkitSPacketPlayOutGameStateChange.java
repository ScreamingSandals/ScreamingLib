package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutGameStateChange;

//TODO: 1.17 support
public class BukkitSPacketPlayOutGameStateChange extends BukkitSPacket implements SPacketPlayOutGameStateChange {
    public BukkitSPacketPlayOutGameStateChange() {
        super(ClassStorage.NMS.PacketPlayOutGameStateChange);
    }

    @Override
    public void setReason(int reason) {
        packet.setField("a", reason);
    }

    @Override
    public void setValue(float value) {
        packet.setField("b", value);
    }
}
