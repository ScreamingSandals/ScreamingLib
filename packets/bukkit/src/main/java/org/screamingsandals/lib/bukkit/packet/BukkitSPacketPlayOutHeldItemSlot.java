package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.common.packet.SPacketPlayOutHeldItemSlot;

public class BukkitSPacketPlayOutHeldItemSlot extends BukkitSPacket implements SPacketPlayOutHeldItemSlot {
    public BukkitSPacketPlayOutHeldItemSlot() {
        super(ClassStorage.NMS.PacketPlayOutHeldItemSlot);
    }

    @Override
    public void setSlot(int slot) {
        packet.setField("a", slot);
    }
}
