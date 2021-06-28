package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutEntityVelocity;
import org.screamingsandals.lib.utils.math.Vector3D;

public class BukkitSPacketPlayOutEntityVelocity extends BukkitSPacket implements SPacketPlayOutEntityVelocity {
    public BukkitSPacketPlayOutEntityVelocity() {
        super(ClassStorage.NMS.PacketPlayOutEntityVelocity);
    }

    @Override
    public void setEntityId(int entityId) {
        packet.setField("a,field_149417_a", entityId);
    }

    @Override
    public void setVelocity(Vector3D velocity) {
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

        packet.setField("b,field_149415_b", (int) (d0 * 8000.00));
        packet.setField("c,field_149416_c", (int) (d1 * 8000.00));
        packet.setField("d,field_149414_d", (int) (d2 * 8000.00));
    }
}
