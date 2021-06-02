package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutAnimation;

public class BukkitSPacketPlayOutAnimation extends BukkitSPacket implements SPacketPlayOutAnimation {
    public BukkitSPacketPlayOutAnimation() {
        super(ClassStorage.NMS.PacketPlayOutAnimation);
    }

    @Override
    public void setEntityId(int entityId) {
        packet.setField("a", entityId);
    }

    @Override
    public void setAnimation(int animationId) {
        packet.setField("b", animationId);
    }
}
