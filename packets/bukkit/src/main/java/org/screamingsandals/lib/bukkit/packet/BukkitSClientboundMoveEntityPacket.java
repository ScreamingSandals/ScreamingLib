package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.nms.accessors.ClientboundMoveEntityPacketAccessor;
import org.screamingsandals.lib.packet.SClientboundMoveEntityPacket;
import org.screamingsandals.lib.utils.math.Vector3D;

public class BukkitSClientboundMoveEntityPacket extends BukkitSPacket implements SClientboundMoveEntityPacket {

    public BukkitSClientboundMoveEntityPacket() {
        super(ClientboundMoveEntityPacketAccessor.getType());
    }

    @Override
    public SClientboundMoveEntityPacket setEntityId(int entityId) {
        packet.setField(ClientboundMoveEntityPacketAccessor.getFieldEntityId(), entityId);
        return this;
    }

    @Override
    public SClientboundMoveEntityPacket setVelocity(Vector3D velocity) {
        if (velocity == null) {
            velocity = new Vector3D(0, 0, 0);
        }
        packet.setField(ClientboundMoveEntityPacketAccessor.getFieldXa(), (short) velocity.getX());
        packet.setField(ClientboundMoveEntityPacketAccessor.getFieldYa(), (short) velocity.getY());
        packet.setField(ClientboundMoveEntityPacketAccessor.getFieldZa(), (short) velocity.getZ());
        return this;
    }

    @Override
    public SClientboundMoveEntityPacket setYaw(byte yaw) {
        packet.setField(ClientboundMoveEntityPacketAccessor.getFieldYRot(), yaw);
        return this;
    }

    @Override
    public SClientboundMoveEntityPacket setPitch(byte pitch) {
        packet.setField(ClientboundMoveEntityPacketAccessor.getFieldXRot(), pitch);
        return this;
    }

    @Override
    public SClientboundMoveEntityPacket setIsOnGround(boolean isOnGround) {
        packet.setField(ClientboundMoveEntityPacketAccessor.getFieldOnGround(), isOnGround);
        return this;
    }

    @Override
    public SClientboundMoveEntityPacket setHasRotation(boolean hasRotation) {
        packet.setField(ClientboundMoveEntityPacketAccessor.getFieldHasRot(), hasRotation);
        return this;
    }

    @Override
    public SClientboundMoveEntityPacket setHasPosition(boolean hasPosition) {
        packet.setField(ClientboundMoveEntityPacketAccessor.getFieldHasPos(), hasPosition);
        return this;
    }

    // Equivalant BukkitSPacketPlayOutEntityLook
    public class Rot extends BukkitSPacket implements SClientboundMoveEntityPacket.Rot {
        public Rot() {
            super(ClientboundMoveEntityPacketAccessor.getType());
            packet.setField(ClientboundMoveEntityPacketAccessor.getFieldXa(), (short) 0);
            packet.setField(ClientboundMoveEntityPacketAccessor.getFieldYa(), (short) 0);
            packet.setField(ClientboundMoveEntityPacketAccessor.getFieldZa(), (short) 0);
            packet.setField(ClientboundMoveEntityPacketAccessor.getFieldHasRot(), true);
            packet.setField(ClientboundMoveEntityPacketAccessor.getFieldHasPos(), false);
        }

        @Override
        public SClientboundMoveEntityPacket.Rot setEntityId(int entityId) {
            packet.setField(ClientboundMoveEntityPacketAccessor.getFieldEntityId(), entityId);
            return this;
        }

        @Override
        public SClientboundMoveEntityPacket.Rot setYaw(byte yaw) {
            packet.setField(ClientboundMoveEntityPacketAccessor.getFieldYRot(), yaw);
            return this;
        }

        @Override
        public SClientboundMoveEntityPacket.Rot setPitch(byte pitch) {
            packet.setField(ClientboundMoveEntityPacketAccessor.getFieldXRot(), pitch);
            return this;
        }

        @Override
        public SClientboundMoveEntityPacket.Rot setOnGround(boolean isOnGround) {
            packet.setField(ClientboundMoveEntityPacketAccessor.getFieldOnGround(), isOnGround);
            return this;
        }
    }
}
