package org.screamingsandals.lib.bukkit;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.common.SPacketPlayOutRemoveEntityEffect;

public class BukkitSPacketPlayOutRemoveEntityEffect extends BukkitSPacket implements SPacketPlayOutRemoveEntityEffect {
    public BukkitSPacketPlayOutRemoveEntityEffect() {
        super(ClassStorage.NMS.PacketPlayOutRemoveEntityEffect);
    }

    @Override
    public void setEntityId(int entityId) {
        packet.setField("a", entityId);
    }

    @Override
    public void setEffect(int effect) {
        packet.setField("b", effect);
    }
}
