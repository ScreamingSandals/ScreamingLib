package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.nms.accessors.ClientboundExplodePacketAccessor;
import org.screamingsandals.lib.packet.SClientboundExplodePacket;
import org.screamingsandals.lib.utils.math.Vector3Df;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.ArrayList;
import java.util.List;

public class BukkitSClientboundExplodePacket extends BukkitSPacket implements SClientboundExplodePacket {

    public BukkitSClientboundExplodePacket() {
        super(ClientboundExplodePacketAccessor.getType());
    }

    @Override
    public SClientboundExplodePacket setX(double x) {
        packet.setField(ClientboundExplodePacketAccessor.getFieldX(), x);
        return this;
    }

    @Override
    public SClientboundExplodePacket setY(double y) {
        packet.setField(ClientboundExplodePacketAccessor.getFieldY(), y);
        return this;
    }

    @Override
    public SClientboundExplodePacket setZ(double z) {
        packet.setField(ClientboundExplodePacketAccessor.getFieldZ(), z);
        return this;
    }

    @Override
    public SClientboundExplodePacket setStrength(float strength) {
        packet.setField(ClientboundExplodePacketAccessor.getFieldPower(), strength);
        return this;
    }

    @Override
    public SClientboundExplodePacket setKnockBackVelocity(Vector3Df knockBack) {
        if (knockBack == null) {
            throw new UnsupportedOperationException("Velocity cannot be null!");
        }

        packet.setField(ClientboundExplodePacketAccessor.getFieldKnockbackX(), knockBack.getX());
        packet.setField(ClientboundExplodePacketAccessor.getFieldKnockbackY(), knockBack.getY());
        packet.setField(ClientboundExplodePacketAccessor.getFieldKnockbackZ(), knockBack.getZ());
        return this;
    }

    @Override
    public SClientboundExplodePacket setBlocks(List<LocationHolder> blockLocations) {
        if (blockLocations == null) {
            throw new UnsupportedOperationException("Invalid block locations provided!");
        }

        final var bukkitBlockLocations = new ArrayList<>();
        blockLocations.forEach(location -> {
            var constructed = Reflect.constructor(ClassStorage.NMS.BlockPosition, int.class, int.class, int.class)
                    .construct(location.getX(), location.getY(), location.getZ());
            bukkitBlockLocations.add(constructed);
        });
        packet.setField(ClientboundExplodePacketAccessor.getFieldToBlow(), bukkitBlockLocations);
        return this;
    }
}
