package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutEntityTeleport;
import org.screamingsandals.lib.world.LocationHolder;

public class BukkitSPacketPlayOutEntityTeleport extends BukkitSPacket implements SPacketPlayOutEntityTeleport {

    public BukkitSPacketPlayOutEntityTeleport() {
        super(ClassStorage.NMS.PacketPlayOutEntityTeleport);
    }

    @Override
    public SPacketPlayOutEntityTeleport setEntityId(int entityId) {
        packet.setField("a,field_149458_a,f_133529_", entityId);
        return this;
    }

    @Override
    public SPacketPlayOutEntityTeleport setLocation(LocationHolder location) {
        if (location == null) {
            throw new UnsupportedOperationException("Location cannot be null!");
        }
        packet.setField("b,field_149456_b,f_133530_", location.getX());
        packet.setField("c,field_149457_c,f_133531_", location.getY());
        packet.setField("d,field_149454_d,f_133532_", location.getZ());
        packet.setField("e,field_149455_e,f_133533_", (byte) ((int) (location.getYaw() * 256.0F / 360.0F)));
        packet.setField("f,field_149453_f,f_133534_", (byte) ((int) (location.getPitch() * 256.0F / 360.0F)));
        return this;
    }

    @Override
    public SPacketPlayOutEntityTeleport setIsOnGround(boolean isOnGround) {
        packet.setField("g,field_179698_g,f_133535_", isOnGround);
        return this;
    }
}
