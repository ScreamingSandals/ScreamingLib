package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutAnimation;

public class BukkitSPacketPlayOutAnimation extends BukkitSPacket implements SPacketPlayOutAnimation {
    public BukkitSPacketPlayOutAnimation() {
        super(ClassStorage.NMS.PacketPlayOutAnimation);
    }

    @Override
    public void setEntityId(int entityId) {
        if (packet.setField("a", entityId) == null) {
            packet.setField("g", entityId);
        }
    }

    @Override
    public void setAnimation(int animationId) {
        if (packet.setField("b", animationId) == null) {
            packet.setField("h", animationId);
        }
    }
}
