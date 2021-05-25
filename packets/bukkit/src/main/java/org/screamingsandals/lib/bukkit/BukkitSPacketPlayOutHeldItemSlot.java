package org.screamingsandals.lib.bukkit;

import org.screamingsandals.lib.common.SPacketPlayOutHeldItemSlot;

public class BukkitSPacketPlayOutHeldItemSlot extends BukkitSPacket implements SPacketPlayOutHeldItemSlot {
    public BukkitSPacketPlayOutHeldItemSlot(Class<?> packetClass) {
        super(packetClass);
    }

    @Override
    public void setSlot(int slot) {
        packet.setField("a", slot);
    }
}
