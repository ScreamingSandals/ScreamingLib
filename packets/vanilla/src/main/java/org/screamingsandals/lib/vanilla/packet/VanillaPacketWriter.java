package org.screamingsandals.lib.vanilla.packet;

import io.netty.buffer.ByteBuf;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.nms.accessors.*;
import org.screamingsandals.lib.packet.PacketWriter;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.BlockDataHolder;

public abstract class VanillaPacketWriter extends PacketWriter {

    private static Integer PROTOCOL = null;

    public VanillaPacketWriter(ByteBuf buffer) {
        super(buffer);
    }

    @Override
    protected int getItemId(MaterialHolder material) {
        return Reflect.fastInvokeResulted(ItemAccessor.getMethodGetId1(), materialHolderToItem(material)).as(Integer.class);
    }

    @Override
    protected int getBlockStateId(BlockDataHolder blockDataHolder) {
        return Reflect.fastInvokeResulted(BlockAccessor.getMethodGetId1(), blockDataToBlockState(blockDataHolder)).as(Integer.class);
        // TODO: check if this works for legacy too
    }

    @Override
    public int protocol() {
        if (PROTOCOL == null) {
            if (SharedConstantsAccessor.getMethodGetProtocolVersion1() != null) {
                PROTOCOL = Reflect.fastInvokeResulted(SharedConstantsAccessor.getMethodGetProtocolVersion1()).as(int.class);
            } else {
                PROTOCOL = Reflect.getFieldResulted(getMinecraftServerInstance(), MinecraftServerAccessor.getFieldStatus())
                        .fastInvokeResulted(ServerStatusAccessor.getMethodGetVersion1())
                        .fastInvokeResulted(ServerStatus_i_VersionAccessor.getMethodGetProtocol1())
                        .as(int.class);
            }
        }
        return PROTOCOL;
    }

    protected abstract Object materialHolderToItem(MaterialHolder material);

    protected abstract Object blockDataToBlockState(BlockDataHolder blockData);

    protected abstract Object getMinecraftServerInstance();
}
