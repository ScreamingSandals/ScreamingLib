package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutRemoveEntityEffect;

public class BukkitSPacketPlayOutRemoveEntityEffect extends BukkitSPacket implements SPacketPlayOutRemoveEntityEffect {

    public BukkitSPacketPlayOutRemoveEntityEffect() {
        super(ClassStorage.NMS.PacketPlayOutRemoveEntityEffect);
    }

    @Override
    public SPacketPlayOutRemoveEntityEffect setEntityId(int entityId) {
        packet.setField("a,field_149079_a,f_132895_", entityId);
        return this;
    }

    @Override
    public SPacketPlayOutRemoveEntityEffect setEffect(int effect) {
        packet.setField("b,field_149078_b,f_132896_", effect);
        return this;
    }
}
