package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutHeldItemSlot;

public class BukkitSPacketPlayOutHeldItemSlot extends BukkitSPacket implements SPacketPlayOutHeldItemSlot {

    public BukkitSPacketPlayOutHeldItemSlot() {
        super(ClassStorage.NMS.PacketPlayOutHeldItemSlot);
    }

    @Override
    public SPacketPlayOutHeldItemSlot setSlot(int slot) {
        packet.setField("a,field_149387_a,f_133069_", slot);
        return this;
    }
}
