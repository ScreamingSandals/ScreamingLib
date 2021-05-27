package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutEntityDestroy;

public class BukkitSPacketPlayOutEntityDestroy extends BukkitSPacket implements SPacketPlayOutEntityDestroy {

    public BukkitSPacketPlayOutEntityDestroy() {
        super(ClassStorage.NMS.PacketPlayOutEntityDestroy);
    }

    @Override
    public void setEntitiesToDestroy(int[] entityIdArray) {
        packet.setField("a", entityIdArray);
    }
}
