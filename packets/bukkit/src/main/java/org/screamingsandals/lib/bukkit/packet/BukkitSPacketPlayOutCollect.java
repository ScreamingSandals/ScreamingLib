package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutCollect;

public class BukkitSPacketPlayOutCollect extends BukkitSPacket implements SPacketPlayOutCollect {

    public BukkitSPacketPlayOutCollect() {
        super(ClassStorage.NMS.PacketPlayOutCollect);
    }

    @Override
    public SPacketPlayOutCollect setEntityId(int entityId) {
        packet.setField("a,field_149357_a,f_133510_", entityId);
        return this;
    }

    @Override
    public SPacketPlayOutCollect setCollectedEntityId(int collectedEntityId) {
        packet.setField("b,field_149356_b,f_133511_", collectedEntityId);
        return this;
    }

    @Override
    public SPacketPlayOutCollect setAmount(int amount) {
        packet.setField("c,field_191209_c,f_133512_", amount);
        return this;
    }
}
