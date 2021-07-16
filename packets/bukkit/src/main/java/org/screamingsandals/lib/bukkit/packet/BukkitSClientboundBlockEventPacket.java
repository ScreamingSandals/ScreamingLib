package org.screamingsandals.lib.bukkit.packet;

import org.bukkit.block.Block;
import org.screamingsandals.lib.nms.accessors.BlockPosAccessor;
import org.screamingsandals.lib.nms.accessors.ClientboundBlockEventPacketAccessor;
import org.screamingsandals.lib.packet.SClientboundBlockEventPacket;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.BlockHolder;
import org.screamingsandals.lib.world.LocationHolder;

public class BukkitSClientboundBlockEventPacket extends BukkitSPacket implements SClientboundBlockEventPacket {

    public BukkitSClientboundBlockEventPacket() {
        super(ClientboundBlockEventPacketAccessor.getType());
    }

    @Override
    public SClientboundBlockEventPacket setBlockLocation(LocationHolder location) {
        if (location == null) {
            throw new UnsupportedOperationException("Location cannot be null!");
        }

        var constructed = Reflect.construct(BlockPosAccessor.getConstructor0(), location.getX(), location.getY(), location.getZ());
        packet.setField(ClientboundBlockEventPacketAccessor.getFieldPos(), constructed);
        return this;
    }

    @Override
    public SClientboundBlockEventPacket setActionId(int actionId) {
        packet.setField(ClientboundBlockEventPacketAccessor.getFieldB0(), actionId);
        return this;
    }

    @Override
    public SClientboundBlockEventPacket setActionParameter(int actionParameter) {
        packet.setField(ClientboundBlockEventPacketAccessor.getFieldB1(), actionParameter);
        return this;
    }

    @Override
    public SClientboundBlockEventPacket setBlockType(BlockHolder block) {
        if (block == null) {
            throw new UnsupportedOperationException("Block cannot be null!");
        }

        final var bukkitBlock = block.as(Block.class);
        var nmsBlock = Reflect.fastInvoke(bukkitBlock, "getNMSBlock");
        if (nmsBlock == null) {
            throw new UnsupportedOperationException("NMSBlock is null!");
        }

        packet.setField(ClientboundBlockEventPacketAccessor.getFieldBlock(), nmsBlock);
        return this;
    }
}
