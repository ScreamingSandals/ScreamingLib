package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutExplosion;
import org.screamingsandals.lib.utils.math.Vector3Df;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.ArrayList;
import java.util.List;

public class BukkitSPacketPlayOutExplosion extends BukkitSPacket implements SPacketPlayOutExplosion {
    public BukkitSPacketPlayOutExplosion() {
        super(ClassStorage.NMS.PacketPlayOutExplosion);
    }

    @Override
    public void setX(double x) {
        packet.setField("a,field_149158_a", x);
    }

    @Override
    public void setY(double y) {
        packet.setField("b,field_149156_b", y);
    }

    @Override
    public void setZ(double z) {
        packet.setField("c,field_149157_c", z);
    }

    @Override
    public void setStrength(float strength) {
        packet.setField("d,field_149154_d", strength);
    }

    @Override
    public void setKnockBackVelocity(Vector3Df knockBack) {
        if (knockBack == null) {
            throw new UnsupportedOperationException("Velocity cannot be null!");
        }
        packet.setField("f,field_149152_f", knockBack.getX());
        packet.setField("g,field_149153_g", knockBack.getY());
        packet.setField("h,field_149159_h", knockBack.getZ());
    }

    @Override
    public void setBlocks(List<LocationHolder> blockLocations) {
        if (blockLocations == null) {
            throw new UnsupportedOperationException("Invalid block locations provided!");
        }
        List<Object> bukkitBlockLocations = new ArrayList<>();
        blockLocations.forEach(location -> {
            var constructed = Reflect.constructor(ClassStorage.NMS.BlockPosition, int.class, int.class, int.class)
                    .construct(location.getX(), location.getY(), location.getZ());
            bukkitBlockLocations.add(constructed);
        });
        packet.setField("e,field_149155_e", bukkitBlockLocations);
    }
}
