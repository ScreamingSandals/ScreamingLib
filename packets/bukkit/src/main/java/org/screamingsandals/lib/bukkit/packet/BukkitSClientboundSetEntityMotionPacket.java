package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.nms.accessors.ClientboundSetEntityMotionPacketAccessor;
import org.screamingsandals.lib.packet.SClientboundSetEntityMotionPacket;
import org.screamingsandals.lib.utils.math.Vector3D;

public class BukkitSClientboundSetEntityMotionPacket extends BukkitSPacket implements SClientboundSetEntityMotionPacket {

    public BukkitSClientboundSetEntityMotionPacket() {
        super(ClientboundSetEntityMotionPacketAccessor.getType());
    }

    @Override
    public SClientboundSetEntityMotionPacket setEntityId(int entityId) {
        packet.setField(ClientboundSetEntityMotionPacketAccessor.getFieldId(), entityId);
        return this;
    }

    @Override
    public SClientboundSetEntityMotionPacket setVelocity(Vector3D velocity) {
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

        packet.setField(ClientboundSetEntityMotionPacketAccessor.getFieldXa(), (int) (d0 * 8000.00));
        packet.setField(ClientboundSetEntityMotionPacketAccessor.getFieldYa(), (int) (d1 * 8000.00));
        packet.setField(ClientboundSetEntityMotionPacketAccessor.getFieldZa(), (int) (d2 * 8000.00));
        return this;
    }
}
