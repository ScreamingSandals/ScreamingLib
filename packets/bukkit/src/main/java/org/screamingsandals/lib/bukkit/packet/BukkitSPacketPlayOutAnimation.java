package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.packet.SPacketPlayOutAnimation;

public class BukkitSPacketPlayOutAnimation extends BukkitSPacket implements SPacketPlayOutAnimation {
    public BukkitSPacketPlayOutAnimation() {
        super(ClassStorage.NMS.PacketPlayOutAnimation);
    }

    @Override
    public void setEntityId(int entityId) {
        if (Version.isVersion(1, 17)) {
            packet.setField("g", entityId);
        } else {
            packet.setField("a,field_148981_a", entityId);
        }
    }

    @Override
    public void setAnimation(int animationId) {
        if (Version.isVersion(1, 17)) {
            packet.setField("h", animationId);
        } else {
            packet.setField("b,field_148980_b", animationId);
        }
    }
}
