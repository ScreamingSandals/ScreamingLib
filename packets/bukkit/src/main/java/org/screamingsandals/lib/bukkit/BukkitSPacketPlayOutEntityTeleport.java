package org.screamingsandals.lib.bukkit;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.common.SPacketPlayOutEntityTeleport;
import org.screamingsandals.lib.entity.EntityLiving;

public class BukkitSPacketPlayOutEntityTeleport extends BukkitSPacket implements SPacketPlayOutEntityTeleport {

    public BukkitSPacketPlayOutEntityTeleport() {
        super(ClassStorage.NMS.PacketPlayOutEntityTeleport);
    }

    @Override
    public void setEntity(EntityLiving entity) {
        if (entity == null) {
            throw new UnsupportedOperationException("Entity cannot be null!");
        }
        packet.setField("a", entity.getEntityId());
        packet.setField("b", entity.getLocation().getX());
        packet.setField("c", entity.getLocation().getY());
        packet.setField("d", entity.getLocation().getZ());
        packet.setField ("e", (byte) ((int) (entity.getLocation().getYaw() * 256.0F / 360.0F)));
        packet.setField ("f", (byte) ((int) (entity.getLocation().getPitch() * 256.0F / 360.0F)));
        packet.setField("g", entity.isOnGround());
    }
}
