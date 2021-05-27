package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.common.packet.SPacketPlayOutAttachEntity;

public class BukkitSPacketPlayOutAttachEntity extends BukkitSPacket implements SPacketPlayOutAttachEntity {
    public BukkitSPacketPlayOutAttachEntity() {
        super(ClassStorage.NMS.PacketPlayOutAttachEntity);
    }

    @Override
    public void setEntityId(int entityId) {
        packet.setField("a", entityId);
    }

    @Override
    public void setHoldingEntityId(int entityId) {
        packet.setField("b", entityId);
    }
}
