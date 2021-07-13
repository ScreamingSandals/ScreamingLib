package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutAttachEntity;

public class BukkitSPacketPlayOutAttachEntity extends BukkitSPacket implements SPacketPlayOutAttachEntity {

    public BukkitSPacketPlayOutAttachEntity() {
        super(ClassStorage.NMS.PacketPlayOutAttachEntity);
    }

    @Override
    public SPacketPlayOutAttachEntity setEntityId(int entityId) {
        packet.setField("a,field_149406_b,f_133160_", entityId);
        return this;
    }

    @Override
    public SPacketPlayOutAttachEntity setHoldingEntityId(int entityId) {
        packet.setField("b,field_149407_c,f_133161_", entityId);
        return this;
    }
}
