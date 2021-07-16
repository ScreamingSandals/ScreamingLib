package org.screamingsandals.lib.bukkit.packet;
import org.bukkit.block.data.BlockData;
import org.bukkit.material.MaterialData;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.nms.accessors.BlockPosAccessor;
import org.screamingsandals.lib.nms.accessors.ClientboundBlockUpdatePacketAccessor;
import org.screamingsandals.lib.packet.SClientboundBlockUpdatePacket;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.BlockDataHolder;
import org.screamingsandals.lib.world.LocationHolder;

public class BukkitSClientboundBlockUpdatePacket extends BukkitSPacket implements SClientboundBlockUpdatePacket {

    public BukkitSClientboundBlockUpdatePacket() {
        super(ClientboundBlockUpdatePacketAccessor.getType());
    }

    @Override
    public SClientboundBlockUpdatePacket setBlockLocation(LocationHolder blockLocation) {
        if (blockLocation == null) {
            throw new UnsupportedOperationException("Block location cannot be null!");
        }

        final var bukkitBlockPos = Reflect.construct(BlockPosAccessor.getConstructor0(), blockLocation.getX(), blockLocation.getY(), blockLocation.getZ());
        packet.setField(ClientboundBlockUpdatePacketAccessor.getFieldPos(), bukkitBlockPos);
        return this;
    }

    @Override
    public SClientboundBlockUpdatePacket setBlockData(BlockDataHolder blockData) {
        if (blockData == null) {
            throw new UnsupportedOperationException("Block data cannot be null!");
        }
        Object bukkitBlockData;
        Object nmsBlockData;
        if (Reflect.has("org.bukkit.block.data.BlockData")) {
            bukkitBlockData = blockData.as(BlockData.class);
            nmsBlockData = Reflect.getMethod(bukkitBlockData, "getState").invoke(bukkitBlockData);
        } else {
            bukkitBlockData = blockData.as(MaterialData.class);
            nmsBlockData = Reflect.getMethod(ClassStorage.NMS.CraftMagicNumbers, "getBlock", MaterialData.class)
                    .invokeStatic(bukkitBlockData);
        }
        packet.setField(ClientboundBlockUpdatePacketAccessor.getFieldBlockState(), nmsBlockData);
        return this;
    }
}
