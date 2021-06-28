package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutCollect;

public class BukkitSPacketPlayOutCollect extends BukkitSPacket implements SPacketPlayOutCollect {
    public BukkitSPacketPlayOutCollect() {
        super(ClassStorage.NMS.PacketPlayOutCollect);
    }

    @Override
    public void setEntityId(int entityId) {
        packet.setField("a,field_149357_a", entityId);
    }

    @Override
    public void setCollectedEntityId(int collectedEntityId) {
        packet.setField("b,field_149356_b", collectedEntityId);
    }

    @Override
    public void setAmount(int amount) {
        packet.setField("c,field_191209_c", amount);
    }
}
