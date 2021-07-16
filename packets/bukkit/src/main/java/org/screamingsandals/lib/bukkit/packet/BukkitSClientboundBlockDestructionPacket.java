package org.screamingsandals.lib.bukkit.packet;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.nms.accessors.ClientboundBlockDestructionPacketAccessor;
import org.screamingsandals.lib.packet.SClientboundBlockDestructionPacket;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.LocationHolder;

public class BukkitSClientboundBlockDestructionPacket extends BukkitSPacket implements SClientboundBlockDestructionPacket {

    public BukkitSClientboundBlockDestructionPacket() {
        super(ClientboundBlockDestructionPacketAccessor.getType());
    }

    @Override
    public SClientboundBlockDestructionPacket setEntityId(int entityId) {
        packet.setField(ClientboundBlockDestructionPacketAccessor.getFieldId(), entityId);
        return this;
    }

    @Override
    public SClientboundBlockDestructionPacket setBlockLocation(LocationHolder blockLocation) {
        if (blockLocation == null) {
            throw new UnsupportedOperationException("Cannot play block break animation on null location!");
        }

        var bukkitBlockPos = Reflect.constructor(ClassStorage.NMS.BlockPosition, int.class, int.class, int.class)
                .construct(blockLocation.getX(), blockLocation.getY(), blockLocation.getZ());

        packet.setField(ClientboundBlockDestructionPacketAccessor.getFieldPos(), bukkitBlockPos);
        return this;
    }

    @Override
    public SClientboundBlockDestructionPacket setDestroyStage(int destroyStage) {
        if (destroyStage < 0 || destroyStage > 9) {
            throw new UnsupportedOperationException("Invalid destroy stage: " + destroyStage);
        }

        packet.setField(ClientboundBlockDestructionPacketAccessor.getFieldProgress(), destroyStage);
        return this;
    }
}
