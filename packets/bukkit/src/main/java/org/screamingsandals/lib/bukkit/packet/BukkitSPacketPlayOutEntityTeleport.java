package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutEntityTeleport;
import org.screamingsandals.lib.world.LocationHolder;

public class BukkitSPacketPlayOutEntityTeleport extends BukkitSPacket implements SPacketPlayOutEntityTeleport {

    public BukkitSPacketPlayOutEntityTeleport() {
        super(ClassStorage.NMS.PacketPlayOutEntityTeleport);
    }

    @Override
    public void setEntityId(int entityId) {
        packet.setField("a,field_149458_a", entityId);
    }

    @Override
    public void setLocation(LocationHolder location) {
        if (location == null) {
            throw new UnsupportedOperationException("Location cannot be null!");
        }
        packet.setField("b,field_149456_b", location.getX());
        packet.setField("c,field_149457_c", location.getY());
        packet.setField("d,field_149454_d", location.getZ());
        packet.setField("e,field_149455_e", (byte) ((int) (location.getYaw() * 256.0F / 360.0F)));
        packet.setField("f,field_149453_f", (byte) ((int) (location.getPitch() * 256.0F / 360.0F)));
    }

    @Override
    public void setIsOnGround(boolean isOnGround) {
        packet.setField("g,field_179698_g", isOnGround);
    }
}
