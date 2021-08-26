package org.screamingsandals.lib.vanilla.packet;

import io.netty.buffer.ByteBuf;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.item.ItemTypeHolder;
import org.screamingsandals.lib.nms.accessors.*;
import org.screamingsandals.lib.packet.PacketWriter;
import org.screamingsandals.lib.utils.reflect.Reflect;

public abstract class VanillaPacketWriter extends PacketWriter {

    private static Integer PROTOCOL = null;

    public VanillaPacketWriter(ByteBuf buffer) {
        super(buffer);
    }

    @Override
    protected int getItemId(ItemTypeHolder material) {
        return Reflect.fastInvokeResulted(ItemAccessor.getMethodGetId1(), materialHolderToItem(material)).as(Integer.class);
    }

    @Override
    protected int getBlockStateId(BlockTypeHolder blockDataHolder) {
        return Reflect.fastInvokeResulted(BlockAccessor.getMethodGetId1(), blockDataToBlockState(blockDataHolder)).as(Integer.class);
        // TODO: check if this works for legacy too
    }

    @Override
    public int protocol() {
        if (PROTOCOL == null) {
            if (SharedConstantsAccessor.getMethodGetProtocolVersion1() != null) {
                PROTOCOL = Reflect.fastInvokeResulted(SharedConstantsAccessor.getMethodGetProtocolVersion1()).as(Integer.class);
            } else {
                PROTOCOL = Reflect.getFieldResulted(getMinecraftServerInstance(), MinecraftServerAccessor.getFieldStatus())
                        .fastInvokeResulted(ServerStatusAccessor.getMethodGetVersion1())
                        .fastInvokeResulted(ServerStatus_i_VersionAccessor.getMethodGetProtocol1())
                        .as(Integer.class);
            }
        }
        return PROTOCOL;
    }

    protected abstract Object materialHolderToItem(ItemTypeHolder material);

    protected abstract Object blockDataToBlockState(BlockTypeHolder blockData);

    protected abstract Object getMinecraftServerInstance();
}
