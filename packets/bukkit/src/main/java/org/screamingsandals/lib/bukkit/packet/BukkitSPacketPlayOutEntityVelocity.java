package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutEntityVelocity;
import org.screamingsandals.lib.utils.math.Vector3D;

public class BukkitSPacketPlayOutEntityVelocity extends BukkitSPacket implements SPacketPlayOutEntityVelocity {

    public BukkitSPacketPlayOutEntityVelocity() {
        super(ClassStorage.NMS.PacketPlayOutEntityVelocity);
    }

    @Override
    public SPacketPlayOutEntityVelocity setEntityId(int entityId) {
        packet.setField("a,field_149417_a,f_133176_", entityId);
        return this;
    }

    @Override
    public SPacketPlayOutEntityVelocity setVelocity(Vector3D velocity) {
        double d3 = 3.9D;

        double d0 = velocity.getX();
        double d1 = velocity.getY();
        double d2 = velocity.getZ();

        if (d0 < -d3) {
            d0 = -d3;
        }

        if (d1 < -d3) {
            d1 = -d3;
        }

        if (d2 < -d3) {
            d2 = -d3;
        }

        if (d0 > d3) {
            d0 = d3;
        }

        if (d1 > d3) {
            d1 = d3;
        }

        if (d2 > d3) {
            d2 = d3;
        }

        packet.setField("b,field_149415_b,f_133177_", (int) (d0 * 8000.00));
        packet.setField("c,field_149416_c,f_133178_", (int) (d1 * 8000.00));
        packet.setField("d,field_149414_d,f_133179_", (int) (d2 * 8000.00));
        return this;
    }
}
