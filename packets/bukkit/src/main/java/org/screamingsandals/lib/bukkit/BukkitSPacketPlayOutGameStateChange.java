package org.screamingsandals.lib.bukkit;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.common.SPacketPlayOutGameStateChange;

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
