package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.packet.SPacketPlayOutAnimation;

public class BukkitSPacketPlayOutAnimation extends BukkitSPacket implements SPacketPlayOutAnimation {

    public BukkitSPacketPlayOutAnimation() {
        super(ClassStorage.NMS.PacketPlayOutAnimation);
    }

    @Override
    public SPacketPlayOutAnimation setEntityId(int entityId) {
        if (Version.isVersion(1, 17)) {
            packet.setField("g,ef_131612_", entityId);
        } else {
            packet.setField("a,field_148981_a", entityId);
        }
        return this;
    }

    @Override
    public SPacketPlayOutAnimation setAnimation(int animationId) {
        if (Version.isVersion(1, 17)) {
            packet.setField("h,f_131613_", animationId);
        } else {
            packet.setField("b,field_148980_b", animationId);
        }
        return this;
    }
}
