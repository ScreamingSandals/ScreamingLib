package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.common.packet.SPacketPlayOutEntityTeleport;
import org.screamingsandals.lib.world.LocationHolder;

public class BukkitSPacketPlayOutEntityTeleport extends BukkitSPacket implements SPacketPlayOutEntityTeleport {

    public BukkitSPacketPlayOutEntityTeleport() {
        super(ClassStorage.NMS.PacketPlayOutEntityTeleport);
    }

    @Override
    public void setEntityId(int entityId) {
        packet.setField("a", entityId);
    }

    @Override
    public void setLocation(LocationHolder location) {
        if (location == null) {
            throw new UnsupportedOperationException("Location cannot be null!");
        }
        packet.setField("b", location.getX());
        packet.setField("c", location.getY());
        packet.setField("d", location.getZ());
        packet.setField("e", (byte) ((int) (location.getYaw() * 256.0F / 360.0F)));
        packet.setField("f", (byte) ((int) (location.getPitch() * 256.0F / 360.0F)));
    }

    @Override
    public void setIsOnGround(boolean isOnGround) {
        packet.setField("g", isOnGround);
    }
}
