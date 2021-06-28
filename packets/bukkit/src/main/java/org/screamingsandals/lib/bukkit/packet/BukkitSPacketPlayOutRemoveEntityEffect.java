package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutRemoveEntityEffect;

public class BukkitSPacketPlayOutRemoveEntityEffect extends BukkitSPacket implements SPacketPlayOutRemoveEntityEffect {
    public BukkitSPacketPlayOutRemoveEntityEffect() {
        super(ClassStorage.NMS.PacketPlayOutRemoveEntityEffect);
    }

    @Override
    public void setEntityId(int entityId) {
        packet.setField("a,field_149079_a", entityId);
    }

    @Override
    public void setEffect(int effect) {
        packet.setField("b,field_149078_b", effect);
    }
}
